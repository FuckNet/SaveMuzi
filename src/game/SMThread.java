package game;

import java.util.Random;
import java.util.Vector;

import game.GamePanel.Status;
import main.SMFrame;
import network.SMNet;
import network.SMQueue;

//SMThread
public class SMThread extends Thread {

	// private GameScreen gameScreen;
	private Status status;
	private GamePanel gamePanel;
	private GameScreen gameScreen;
	private SMQueue smQueue;
	private Player players[];
	private Vector<Object> bullets;
	private Vector<Object> enemies;
	private Vector<Object> effects;
	private Vector<Object> items;
	private SMNet smNet;

	private int playerNo;

	public static int seed;

	// 게임 상태 변수
	private int level;
	public int cnt; // 그림을 그리는데 사용하는 int 변수
	public static int gameCnt; // 게임 도중에 사용되는 이벤트 처리성 int 변수
	private boolean isBoss;

	public SMThread() {
		gameInit();
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		gameScreen.setBullets(bullets);
		gameScreen.setEnemies(enemies);
		gameScreen.setEffects(effects);
		gameScreen.setItems(items);
		gameScreen.init();
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.smNet = gamePanel.getSMNet();
		this.playerNo = smNet.getPlayerNum();
		players = gamePanel.getPlayer();
		bullets = gamePanel.getBullets();
	}

	private void gameInit() {
		cnt = 0;
		gameCnt = 0;
		level = 1;
		isBoss = false;
		enemies = new Vector<Object>();
		effects = new Vector<Object>();
		items = new Vector<Object>();
		status = Status.GameStart;
		smQueue = SMQueue.getSMQueue();
		Bullet.bulletInit();
		Enemy.enemyInit();
		Item.itemInit(this);
		Effect.effectInit();
		Player.playerInit(this);
	}

	public void run() {
		long preTime; // 루프 간격을 조절하기 위한 시간 체크 값
		int delay = 17; // 루프 딜레이, 1/1000초 단위. > 17/1000초 = 58(프레임/초)
		smQueue.clear();
		try {
			while (true) {
				preTime = System.currentTimeMillis();

				// Thread.sleep(50);
				// gamePanel.repaint();
				seed = (seed + 1) % 255;
				GamePanel.rnd = new Random(seed);
				process();
				keyProcess();

				// for(int i = 0; i < 100; i++) {
				// keyProcess();
				// Thread.sleep(1);
				// }

				// Thread.sleep(100);
				// System.out.println(preTime);
				// gamePanel.repaint();
				if (gameCnt % 2 == 0) {
					smNet.sendMSGUDP(
							"LOC " + playerNo + " " + players[playerNo].getX() + " " + players[playerNo].getY() + "/");
				}
				gameScreen.repaint();
				if (System.currentTimeMillis() - preTime < delay)
					Thread.sleep(delay - System.currentTimeMillis() + preTime);
				// 게임 루프를 처리하는데 걸린 시간을 체크해서 딜레이값에서 차감하여 딜레이를 일정하게 유지한다.
				// 루프 실행 시간이 딜레이 시간보다 크다면 게임 속도가 느려지게 된다.
				if (status != Status.GameOver)
					cnt++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void process() {
		switch (status) {
		case GameStart:// 스타트
			processPlayer();
			if (players[1].getMode() == 2)
				status = Status.PlayScreen;
			break;
		case PlayScreen:// 게임화면
			processPlayer();
			process_ENEMY();
			process_BULLET();
			process_EFFECT();
			process_GAMEFLOW();
			process_ITEM();
			break;
		case GameOver:// 게임오버
			process_ENEMY();
			process_BULLET();
			process_GAMEFLOW();
			break;
		case Pause:// 일시정지
			break;
		default:
			break;
		}
		if (status != Status.GameOver)
			gameCnt++;
	}

	private void processPlayer() {
		for (int i = 1; i < players.length; i++) {
			players[i].process();
		}
	}

	public synchronized void keyProcess() {
		String msg = smQueue.getMSG();
		if (msg == null || msg.equals(""))
			return;
		String splitMsg[] = msg.split(" ");
		int playerNum;
		for (int i = 0; i < splitMsg.length; i++) {
			playerNum = Integer.parseInt(splitMsg[0]);
			players[playerNum].setState(Integer.parseInt(splitMsg[2]), splitMsg[1].equals("P"));
		}
	}

	public void process_ENEMY() {
		int i, dist;
		Enemy ebuff;
		Player pbuff;
		Effect expl;

		for (i = 0; i < enemies.size(); i++) {
			ebuff = (Enemy) (enemies.elementAt(i));
			if (!ebuff.move()) {
				removeObject(enemies, enemies.get(i));
				// enemies.removeElementAt(i);
			}
		}
		// 플레이어와 적이 부딪칠 경우
		for (i = 1; i < players.length; i++) {
			pbuff = players[i];
			for (int j = 0; j < enemies.size(); j++) {
				ebuff = (Enemy) (enemies.elementAt(j));
				dist = GetDistance(pbuff.center.x, pbuff.center.y, ebuff.center.x, ebuff.center.y);
				if (dist < ebuff.hitrange && pbuff.getMode() != 3) {
					if (pbuff.getShield() == 0) {
						pbuff.setMode(3);
						pbuff.setCnt(30);
						// removeObject(bullets, bullets.get(i));
						// bullets.remove(i);
						expl = new Effect(0, ebuff.dis.x * 100, ebuff.dis.y * 100 - Effect.EFHEIGHT * 100 / 2, 0);
						addObject(effects, expl);
						// effects.add(expl);
						int temp = pbuff.getLife() - 1;
						pbuff.setLife(temp);
						if (temp <= 0) {
							status = Status.GameOver;
							gameCnt = 0;
						}
					} else {// 실드가 있을 경우
						pbuff.setShield(pbuff.getShield() - 1);
						pbuff.setMode(3);
						pbuff.setCnt(30);
						// removeObject(bullets, bullets.get(i));
						// bullets.remove(i);
					}
				}
			}
		}
	}

	public void process_BULLET() {
		Bullet buff;
		Enemy ebuff;
		Effect expl;
		int i, j, dist;
		for (int k = 1; k < players.length; k++) {
			for (i = 0; i < bullets.size(); i++) {
				buff = (Bullet) (bullets.elementAt(i));
				if (k == 1)
					buff.move();
				if (buff.dis.x < 10 || buff.dis.x > SMFrame.SCREEN_WIDTH + 10 || buff.dis.y < 10
						|| buff.dis.y > SMFrame.SCREEN_HEIGHT + 10) {
					removeObject(bullets, bullets.get(i));
					// bullets.remove(i);// 화면 밖으로 나가면 총알 제거
					continue;
				}
				if (buff.from > 0) {// 플레이어가 쏜 총알이 적에게 명중 판정
					for (j = 0; j < enemies.size(); j++) {
						ebuff = (Enemy) (enemies.elementAt(j));
						dist = GetDistance(buff.center.x, buff.center.y, ebuff.center.x, ebuff.center.y);
						// if(dist<1500) {//중간점 거리가 명중 판정이 가능한 범위에 왔을 때
						if (dist < ebuff.hitrange) {// 중간점 거리가 명중 판정이 가능한 범위에 왔을
													// 때 -
							// hitrange : 적 캐릭터마다 그림에 따라
							// 명중판정되는 범위가 다르다
							ebuff.life -= buff.power;
							if (ebuff.life <= 0) {// 적 라이프 감소
								Item tem;
								removeObject(enemies, enemies.get(j));
								// enemies.remove(j);// 적 캐릭터 소거
								expl = new Effect(0, ebuff.pos.x, buff.pos.y - Effect.EFHEIGHT / 2, 0);
								addObject(effects, expl);
								// effects.add(expl);// 폭발 이펙트 추가
								// Item tem=new Item(ebuff.pos.x, buff.pos.y,
								// RAND(1,(level+1)*20)/((level+1)*20));//난수 결과가
								// 최대값일 때만 생성되는 아이템이 1이 된다
								if (ebuff.kind == 1) {
									if (gameCnt < 2100)
										gameCnt = 2100;
									tem = new Item(ebuff.pos.x, buff.pos.y, 3);
								} else {
									int itemKind = GamePanel.RAND(1, 100);
									if (itemKind <= 65)
										tem = new Item(ebuff.pos.x, buff.pos.y, 0);
									else if (itemKind <= 90)
										tem = new Item(ebuff.pos.x, buff.pos.y, 2);
									else if (itemKind <= 95)
										tem = new Item(ebuff.pos.x, buff.pos.y, 3);
									else
										tem = new Item(ebuff.pos.x, buff.pos.y, 1);
								}
								addObject(items, tem);
								// items.add(tem);
							}
							// expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
							expl = new Effect(0, buff.pos.x, buff.pos.y - Effect.EFHEIGHT * 100 / 2, 0);

							addObject(effects, expl);
							// effects.add(expl);
							players[buff.from].addScore(1);// 점수 추가
							removeObject(bullets, bullets.get(i));
							// bullets.remove(i);// 총알 소거
							break;// 총알이 소거되었으므로 루프 아웃
						}
					}
				} else {// 적이 쏜 총알이 플레이어에게 명중 판정
					if (players[k].getMode() != 2)
						continue;
					dist = GetDistance(players[k].center.x, players[k].center.y, buff.center.x, buff.center.y);
					if (dist < 1000) {
						if (players[k].getShield() == 0) {
							players[k].setMode(3);
							players[k].setCnt(30);
							// bullets.remove(i);
							expl = new Effect(0, buff.dis.x * 100, buff.dis.y * 100 - Effect.EFHEIGHT * 100 / 2, 0);
							addObject(effects, expl);
							// effects.add(expl);
							int temp = players[k].getLife() - buff.power;
							players[k].setLife(temp);
							removeObject(bullets, bullets.get(i));
							players[k].powerDown();
							if (temp <= 0) {
								status = Status.GameOver;
								gameCnt = 0;
							}
						} else {// 실드가 있을 경우
							players[k].setShield(players[k].getShield() - 1);
							removeObject(bullets, bullets.get(i));
							// bullets.remove(i);
						}
					}
				}
			}
		}
	}

	public void process_EFFECT() {
		Effect buff;
		for (int i = 0; i < effects.size(); i++) {
			buff = (Effect) (effects.elementAt(i));
			if (cnt % 3 == 0)
				buff.cnt--;
			if (buff.cnt == 0)
				removeObject(effects, effects.get(i));
			// effects.removeElementAt(i);
		}
	}

	public void process_GAMEFLOW() {
		int control = 0;
		int newy = 0, mode = 0;
		// 보스 관련 수정사항 추가
		if (isBoss) {
			// 보스가 생성되어 있는 상황의 처리
			if (level > 1) {// 게임 레벨이 2 이상이면, 보스전 도중에 소형 캐릭터들이 지원기로 나온다
				// 지원기 등장 시나리오
				// : 게임카운트(gamecnt) 0~200 : 지원기 없음
				// : 게임카운트(gamecnt) 801~1000 : 지원기 60카운트 단위로 등장
				// : 게임카운트(gamecnt) 1601~2199 : 지원기 30카운터 단위로 등장
				if (800 < gameCnt && gameCnt < 1000) {// 보스가 등장하고 시간이 좀 지나서 소형
					// 캐릭터들이 덤벼오기
					// 시작한다
					if (gameCnt % 60 == 0) {
						newy = GamePanel.RAND(30, SMFrame.SCREEN_HEIGHT - 50) * 100;
						if (newy < 24000)
							mode = 0;
						else
							mode = 1;
						Enemy en = new Enemy(gamePanel, 0, SMFrame.SCREEN_WIDTH * 100, newy, 0, mode, level);
						enemies.add(en);
					}
				} else if (1600 < gameCnt && gameCnt < 2200) {// 보스전이 후반부에 들어서면서
					// 소형 지원기들의 공격이
					// 거세진다
					if (gameCnt % 30 == 0) {
						Enemy en;
						newy = GamePanel.RAND(30, SMFrame.SCREEN_HEIGHT - 30) * 100;
						if (newy < 24000)
							mode = 0;
						else
							mode = 1;
						if (level > 1 && GamePanel.RAND(1, 100) < level * 10)
							en = new Enemy(gamePanel, 2, SMFrame.SCREEN_WIDTH * 100, newy, 2, 0, level);
						else
							en = new Enemy(gamePanel, 0, SMFrame.SCREEN_WIDTH * 100, newy, 0, mode, level);
						enemies.add(en);
					}
				}
			}
			if (gameCnt > 2210) {// 보스전 타임 아웃으로 보스전을 종료한다
				isBoss = false;
				gameCnt = 0;
			}
		} else {
			if (gameCnt < 500)
				control = 1;
			else if (gameCnt < 1000)
				control = 2;
			else if (gameCnt < 1300)
				control = 0;
			else if (gameCnt < 1700)
				control = 1;
			else if (gameCnt < 2000)
				control = 2;
			else if (gameCnt < 2400)
				control = 3;
			else {
				// 기존에 레벨만 올려주던 부분에서, 레벨을 올려주면서 보스 캐릭터를 등장시킨다
				isBoss = true;
				Enemy en = new Enemy(gamePanel, 1, SMFrame.SCREEN_WIDTH * 100, 24000, 1, 0, level);
				addObject(enemies, en);
				// enemies.add(en);
				gameCnt = 0;
				level++;
			}
			if (control > 0) {
				newy = GamePanel.RAND(30, SMFrame.SCREEN_HEIGHT - 30) * 100;
				if (newy < 24000)
					mode = 0;
				else
					mode = 1;
			}
			Enemy en;
			switch (control) {
			case 1:
				if (gameCnt % 90 == 0) {
					if (GamePanel.RAND(1, 3) != 3 && level > 0)
						en = new Enemy(gamePanel, 2, SMFrame.SCREEN_WIDTH * 100, newy, 2, mode, level);
					else
						en = new Enemy(gamePanel, 0, SMFrame.SCREEN_WIDTH * 100, newy, 0, mode, level);
					addObject(enemies, en);
					// enemies.add(en);
				}
				break;
			case 2:
				if (gameCnt % 50 == 0) {
					if (GamePanel.RAND(1, 3) != 3 && level > 0)
						en = new Enemy(gamePanel, 2, SMFrame.SCREEN_WIDTH * 100, newy, 2, mode, level);
					else
						en = new Enemy(gamePanel, 0, SMFrame.SCREEN_WIDTH * 100, newy, 0, mode, level);
					addObject(enemies, en);
					// enemies.add(en);
				}
				break;
			case 3:
				if (gameCnt % 20 == 0) {
					if (GamePanel.RAND(1, 3) != 3 && level > 0)
						en = new Enemy(gamePanel, 2, SMFrame.SCREEN_WIDTH * 100, newy, 2, mode, level);
					else
						en = new Enemy(gamePanel, 0, SMFrame.SCREEN_WIDTH * 100, newy, 0, mode, level);
					addObject(enemies, en);
					// enemies.add(en);
				}
				break;
			}
		}
	}

	public void process_ITEM() {
		int i, dist;
		Item buff;
		for (int j = 1; j < players.length; j++) {
			for (i = 0; i < items.size(); i++) {
				buff = (Item) (items.elementAt(i));
				dist = GetDistance(players[j].center.x, players[j].center.y, buff.center.x, buff.center.y);
				if (dist < 1000) {// 아이템 획득
					switch (buff.kind) {
					case 0:// 일반 득점
						players[j].addScore(100);
						break;
					case 1:// 실드
						players[j].setShield(5);
						break;
					case 2:// 전멸 아이템
							// Enemy ebuff;
							// Effect expl;

						// 적 전멸 효과
						Effect expl;
						for (int k = 0; k < enemies.size(); k++) {
							Enemy ebuff = (Enemy) (enemies.elementAt(k));
							if (ebuff == null)
								continue;// 만일 해당 인덱스에 적 캐릭터가 생성되어있지 않을 경우를 대비
							if (ebuff.kind == 1) {// 해당 인덱스에 할당된 적 캐릭터가 보스 캐릭터일
								// 경우는 전멸에 해당하지 않고 HP 10 감소
								// 줄인다. 1 이하라면 1.
								players[j].addScore(100);
								ebuff.life = ebuff.life - 10*level;
								if (ebuff.life <= 1)
									ebuff.life = 1;
								continue;
							}
							expl = new Effect(0, buff.pos.x, buff.pos.y, 0);
							if (ebuff.life-- <= 0) {// 적 라이프 감소
								if (ebuff.kind == 1) {
									if (gameCnt < 2100)
										gameCnt = 2100;
								}
								removeObject(enemies, enemies.get(j));
								// enemies.remove(j);// 적 캐릭터 소거
								expl = new Effect(0, ebuff.pos.x, buff.pos.y, 0);
								addObject(effects, expl);
								// effects.add(expl);// 폭발 이펙트 추가
								// Item tem=new Item(ebuff.pos.x, buff.pos.y,
								// RAND(1,(level+1)*20)/((level+1)*20));//난수 결과가
								// 최대값일 때만 생성되는 아이템이 1이 된다
								int itemKind = GamePanel.RAND(1, 100);
								Item tem;
								if (itemKind <= 65)
									tem = new Item(ebuff.pos.x, buff.pos.y, 0);
								else if (itemKind <= 90)
									tem = new Item(ebuff.pos.x, buff.pos.y, 2);
								else if (itemKind <= 95)
									tem = new Item(ebuff.pos.x, buff.pos.y, 3);
								else
									tem = new Item(ebuff.pos.x, buff.pos.y, 1);
								addObject(items, tem);
								// items.add(tem);
							}
							// expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
							// effects.add(expl);
							// score++;// 점수 추가
							expl = new Effect(0, ebuff.pos.x, ebuff.pos.y, 0);
							addObject(effects, expl);
							// effects.add(expl);// 폭발 이펙트 추가
							ebuff.pos.x = -10000;// 다음 처리에서 소거될 수 있도록
							players[j].addScore(50);
							removeObject(enemies, ebuff);
							// enemies.remove(ebuff);//적 캐릭터 소거
						}

						// 적 총알 전부 소거
						for (int k = 0; k < bullets.size(); k++) {
							Bullet bbuff = (Bullet) (bullets.elementAt(k));
							if (bbuff.from == 0) {
								bbuff.pos.x = -10000;
								players[j].addScore(1);
							}
							removeObject(bullets, bbuff);
							// bullets.remove(bbuff);
						}
						break;
					case 3:
						players[j].powerUp();
						break;
					}
					removeObject(items, items.get(i));
					// items.remove(i);
				} else if (j == players.length - 1) {
					if (buff.move())
						removeObject(items, items.get(i));
				}
				// items.remove(i);
			}
		}
	}

	public synchronized void addObject(Vector<Object> vec, Object instance) {
		vec.add(instance);
		// gamePanel.add(instance);
	}

	public void removeObject(Vector<Object> vec, Object instance) {
		vec.remove(instance);
		// gamePanel.remove(instance);
		// System.out.println(instance.getClass().getName() + " 삭제, 현재 : " +
		// vec.size());
	}

	public int GetDistance(int x1, int y1, int x2, int y2) {
		return Math.abs((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	public int getLevel() {
		return this.level;
	}
}