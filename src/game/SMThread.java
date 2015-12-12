package game;

import java.util.Vector;

import javax.swing.JComponent;

import game.GamePanel.Status;
import main.SMFrame;
import network.SMQueue;

public class SMThread extends Thread {

	// private GameScreen gameScreen;
	private Status status;
	private GamePanel gamePanel;
	private SMQueue smQueue;
	private Player players[];
	private Vector<JComponent> bullets;
	private Vector<JComponent> enemies;
	private Vector<JComponent> effects;
	private Vector<JComponent> items;

	// 게임 상태 변수
	private int score;
	private int level;
	private int cnt; // 그림을 그리는데 사용하는 int 변수
	public static int gameCnt; // 게임 도중에 사용되는 이벤트 처리성 int 변수
	private boolean isBoss;

	public SMThread() {
		gameInit();
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		players = gamePanel.getPlayer();
		bullets = gamePanel.getBullets();
	}

	private void gameInit() {
		cnt = 0;
		gameCnt = 0;
		score = 0;
		level = 1;
		isBoss = false;
		enemies = new Vector<JComponent>();
		effects = new Vector<JComponent>();
		items = new Vector<JComponent>();
		status = Status.GameStart;
		smQueue = SMQueue.getSMQueue();
		Bullet.bulletInit();
		Enemy.enemyInit();
	}

	public void run() {
		long preTime; // 루프 간격을 조절하기 위한 시간 체크 값
		int delay = 17; // 루프 딜레이, 1/1000초 단위. > 17/1000초 = 58(프레임/초)
		try {
			while (true) {
				preTime = System.currentTimeMillis();

				// Thread.sleep(50);
				// gamePanel.repaint();
				process();
				// keyProcess();

				// for(int i = 0; i < 100; i++) {
				// keyProcess();
				// Thread.sleep(1);
				// }

				// Thread.sleep(100);
				// System.out.println(preTime);
				gamePanel.repaint();
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
			if(players[1].getMode()==2) status=Status.PlayScreen;
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
		if (msg.equals(null) || msg.equals(""))
			return;

		String splitMsg[] = msg.split(" ");
		int playerNum;
		for (int i = 0; i < splitMsg.length; i++) {
			playerNum = Integer.parseInt(splitMsg[0]);
			players[playerNum].setState(Integer.parseInt(splitMsg[2]), splitMsg[1].equals("P"));
		}

	}
	public void process_ENEMY() {
		int i;
		Enemy buff;
		
		for (i = 0; i < enemies.size(); i++) {
			buff = (Enemy) (enemies.elementAt(i));
			if (!buff.move()) {
				removeObject(enemies, enemies.get(i));
				//enemies.removeElementAt(i);
			}
		}
	}
	public void process_BULLET() {
		Bullet buff;
		Enemy ebuff;
		Effect expl;
		int i, j, dist;
		for (i = 0; i < bullets.size(); i++) {
			buff = (Bullet) (bullets.elementAt(i));
			buff.move();
			if (buff.dis.x < 10 || buff.dis.x > SMFrame.SCREEN_WIDTH + 10 || buff.dis.y < 10
					|| buff.dis.y > SMFrame.SCREEN_HEIGHT + 10) {
				removeObject(bullets, bullets.get(i));
				//bullets.remove(i);// 화면 밖으로 나가면 총알 제거
				continue;
			}
			if (buff.from == 0) {// 플레이어가 쏜 총알이 적에게 명중 판정
				for (j = 0; j < enemies.size(); j++) {
					ebuff = (Enemy) (enemies.elementAt(j));
					dist = GetDistance(buff.dis.x, buff.dis.y, ebuff.dis.x, ebuff.dis.y);
					// if(dist<1500) {//중간점 거리가 명중 판정이 가능한 범위에 왔을 때
					if (dist < ebuff.hitrange) {// 중간점 거리가 명중 판정이 가능한 범위에 왔을 때 -
												// hitrange : 적 캐릭터마다 그림에 따라
												// 명중판정되는 범위가 다르다
						if (ebuff.life-- <= 0) {// 적 라이프 감소
							if (ebuff.kind == 1) {
								if (gameCnt < 2100)
									gameCnt = 2100;
							}
							removeObject(enemies, enemies.get(j));
							//enemies.remove(j);// 적 캐릭터 소거
							expl = new Effect(0, ebuff.pos.x, buff.pos.y, 0);
							effects.add(expl);// 폭발 이펙트 추가
							// Item tem=new Item(ebuff.pos.x, buff.pos.y,
							// RAND(1,(level+1)*20)/((level+1)*20));//난수 결과가
							// 최대값일 때만 생성되는 아이템이 1이 된다
							int itemKind = GamePanel.RAND(1, 100);
							Item tem;
							if (itemKind <= 70)
								tem = new Item(ebuff.pos.x, buff.pos.y, 0);
							else if (itemKind <= 95)
								tem = new Item(ebuff.pos.x, buff.pos.y, 2);
							else
								tem = new Item(ebuff.pos.x, buff.pos.y, 1);
							addObject(items, tem);
							//items.add(tem);
						}
						// expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
						expl = new Effect(0, buff.pos.x, buff.pos.y, 0);
						addObject(effects, expl);
						//effects.add(expl);
						score++;// 점수 추가
						removeObject(bullets, bullets.get(i));
						//bullets.remove(i);// 총알 소거
						break;// 총알이 소거되었으므로 루프 아웃
					}
				}
			} else {// 적이 쏜 총알이 플레이어에게 명중 판정
				for (int k = 1; k < players.length; k++) {
					if (players[k].getMode() != 2)
						continue;
					dist = GetDistance(players[k].getX(), players[k].getY(), buff.dis.x, buff.dis.y);
					if (dist < 500) {
						if (players[k].getShield() == 0) {
							players[k].setMode(3);
							players[k].setCnt(30);
							removeObject(bullets, bullets.get(i));
							//bullets.remove(i);
							expl = new Effect(0, players[k].getX() * 100 - 2000, players[k].getX() * 100, 0);
							addObject(effects, expl);
							//effects.add(expl);
							int temp = players[k].getLife() - 1;
							players[k].setLife(temp);
							System.out.println("라이프 1 감소 -> " + temp);
							if (temp <= 0) {
								status = Status.GameOver;
								gameCnt = 0;
							}
						} else {// 실드가 있을 경우
							players[k].setShield(players[k].getShield() - 1);
							removeObject(bullets, bullets.get(i));
							//bullets.remove(i);
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
				//effects.removeElementAt(i);
		}
	}
	public void process_GAMEFLOW() {
		int control = 0;
		int newy = 0, mode = 0;
		// 보스 관련 수정사항 추가
		if (isBoss) {
			// 보스가 생성되어 있는 상황의 처리
			// if(level>1){// 게임 레벨이 2 이상이면, 보스전 도중에 소형 캐릭터들이 지원기로 나온다
			// //지원기 등장 시나리오
			// // : 게임카운트(gamecnt) 0~200 : 지원기 없음
			// // : 게임카운트(gamecnt) 801~1000 : 지원기 60카운트 단위로 등장
			// // : 게임카운트(gamecnt) 1601~2199 : 지원기 30카운터 단위로 등장
			// if(800<gamecnt&&gamecnt<1000){// 보스가 등장하고 시간이 좀 지나서 소형 캐릭터들이 덤벼오기
			// 시작한다
			// if(gamecnt%60==0) {
			// newy=RAND(30,gScreenHeight-30)*100;
			// if(newy<24000) mode=0; else mode=1;
			// Enemy en=new Enemy(this, 0, gScreenWidth*100, newy, 0,mode);
			// enemies.add(en);
			// }
			// }else
			// if(1600<gamecnt&&gamecnt<2200){// 보스전이 후반부에 들어서면서 소형 지원기들의 공격이
			// 거세진다
			// if(gamecnt%30==0) {
			// Enemy en;
			// newy=RAND(30,gScreenHeight-30)*100;
			// if(newy<24000) mode=0; else mode=1;
			// if(level>1&&RAND(1,100)<level*10)
			// en=new Enemy(this, 2, gScreenWidth*100, newy, 2,0);
			// else
			// en=new Enemy(this, 0, gScreenWidth*100, newy, 0,mode);
			// enemies.add(en);
			// }
			// }
			// }
			// if(gameCnt>2210){// 보스전 타임 아웃으로 보스전을 종료한다
			// isBoss=false;
			// gameCnt=0;
			// System.out.println("보스 타임아웃");
			// }
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
				System.out.println("보스 등장");
				isBoss = true;
				Enemy en = new Enemy(gamePanel, 1, SMFrame.SCREEN_WIDTH * 100, 24000, 1, 0, level);
				addObject(enemies, en);
				//enemies.add(en);
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
					//enemies.add(en);
				}
				break;
			case 2:
				if (gameCnt % 50 == 0) {
					if (GamePanel.RAND(1, 3) != 3 && level > 0)
						en = new Enemy(gamePanel, 2, SMFrame.SCREEN_WIDTH * 100, newy, 2, mode, level);
					else
						en = new Enemy(gamePanel, 0, SMFrame.SCREEN_WIDTH * 100, newy, 0, mode, level);
					addObject(enemies, en);
					//enemies.add(en);
				}
				break;
			case 3:
				if (gameCnt % 20 == 0) {
					if (GamePanel.RAND(1, 3) != 3 && level > 0)
						en = new Enemy(gamePanel, 2, SMFrame.SCREEN_WIDTH * 100, newy, 2, mode, level);
					else
						en = new Enemy(gamePanel, 0, SMFrame.SCREEN_WIDTH * 100, newy, 0, mode, level);
					addObject(enemies, en);
					//enemies.add(en);
				}
				break;
			}
		}
	}
	public void process_ITEM() {
		int i, dist;
		Item buff;
		for (i = 0; i < items.size(); i++) {
			buff = (Item) (items.elementAt(i));
			for (int j = 1; j < players.length; j++) {
				dist = GetDistance(players[j].getX(), players[j].getY(), buff.dis.x, buff.dis.y);
				if (dist < 1000) {// 아이템 획득
					switch (buff.kind) {
					case 0:// 일반 득점
						score += 100;
						break;
					case 1:// 실드
						players[j].setShield(5);
						break;
					case 2:// 전멸 아이템
							// Enemy ebuff;
							// Effect expl;

						// 적 전멸 효과
						int l = enemies.size();
						Effect expl;
						for (int k = 0; k < l; k++) {
							Enemy ebuff = (Enemy) (enemies.elementAt(k));
							if (ebuff == null)
								continue;// 만일 해당 인덱스에 적 캐릭터가 생성되어있지 않을 경우를 대비
							if (ebuff.kind == 1) {// 해당 인덱스에 할당된 적 캐릭터가 보스 캐릭터일
													// 경우는 전멸에 해당하지 않고 HP만 반으로
													// 줄인다. 1 이하라면 1.
								score += 300;
								ebuff.life = ebuff.life / 2;
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
								//enemies.remove(j);// 적 캐릭터 소거
								expl = new Effect(0, ebuff.pos.x, buff.pos.y, 0);
								addObject(effects, expl);
								//effects.add(expl);// 폭발 이펙트 추가
								// Item tem=new Item(ebuff.pos.x, buff.pos.y,
								// RAND(1,(level+1)*20)/((level+1)*20));//난수 결과가
								// 최대값일 때만 생성되는 아이템이 1이 된다
								int itemKind = GamePanel.RAND(1, 100);
								Item tem;
								if (itemKind <= 70)
									tem = new Item(ebuff.pos.x, buff.pos.y, 0);
								else if (itemKind <= 95)
									tem = new Item(ebuff.pos.x, buff.pos.y, 2);
								else
									tem = new Item(ebuff.pos.x, buff.pos.y, 1);
								addObject(items, tem);
								//items.add(tem);
							}
							// expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
							//effects.add(expl);
							//score++;// 점수 추가
							expl = new Effect(0, ebuff.pos.x, ebuff.pos.y, 0);
							addObject(effects, expl);
							//effects.add(expl);// 폭발 이펙트 추가
							ebuff.pos.x = -10000;// 다음 처리에서 소거될 수 있도록
							score += 50;
							// enemies.remove(ebuff);//적 캐릭터 소거
						}

						// 적 총알 전부 소거
						l = bullets.size();
						for (int k = 0; k < l; k++) {
							Bullet bbuff = (Bullet) (bullets.elementAt(k));
							if (bbuff.from != 0) {
								bbuff.pos.x = -10000;
								score++;
							}
							// bullets.remove(bbuff);
						}
						break;
					}
					removeObject(items, items.get(i));
					//items.remove(i);
				} else if (buff.move())
					removeObject(items, items.get(i));
					//items.remove(i);
			}
		}
	}

	public void addObject(Vector<JComponent> vec, JComponent instance) {
		vec.add(instance);
		gamePanel.add(instance);
	}
	
	public void removeObject(Vector<JComponent> vec, JComponent instance) {
		vec.remove(instance);
		gamePanel.remove(instance);
	}
	
	public int GetDistance(int x1, int y1, int x2, int y2) {
		return Math.abs((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
}