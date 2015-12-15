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

	// ���� ���� ����
	private int level;
	public int cnt; // �׸��� �׸��µ� ����ϴ� int ����
	public static int gameCnt; // ���� ���߿� ���Ǵ� �̺�Ʈ ó���� int ����
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
		long preTime; // ���� ������ �����ϱ� ���� �ð� üũ ��
		int delay = 17; // ���� ������, 1/1000�� ����. > 17/1000�� = 58(������/��)
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
				// ���� ������ ó���ϴµ� �ɸ� �ð��� üũ�ؼ� �����̰����� �����Ͽ� �����̸� �����ϰ� �����Ѵ�.
				// ���� ���� �ð��� ������ �ð����� ũ�ٸ� ���� �ӵ��� �������� �ȴ�.
				if (status != Status.GameOver)
					cnt++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void process() {
		switch (status) {
		case GameStart:// ��ŸƮ
			processPlayer();
			if (players[1].getMode() == 2)
				status = Status.PlayScreen;
			break;
		case PlayScreen:// ����ȭ��
			processPlayer();
			process_ENEMY();
			process_BULLET();
			process_EFFECT();
			process_GAMEFLOW();
			process_ITEM();
			break;
		case GameOver:// ���ӿ���
			process_ENEMY();
			process_BULLET();
			process_GAMEFLOW();
			break;
		case Pause:// �Ͻ�����
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
		// �÷��̾�� ���� �ε�ĥ ���
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
					} else {// �ǵ尡 ���� ���
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
					// bullets.remove(i);// ȭ�� ������ ������ �Ѿ� ����
					continue;
				}
				if (buff.from > 0) {// �÷��̾ �� �Ѿ��� ������ ���� ����
					for (j = 0; j < enemies.size(); j++) {
						ebuff = (Enemy) (enemies.elementAt(j));
						dist = GetDistance(buff.center.x, buff.center.y, ebuff.center.x, ebuff.center.y);
						// if(dist<1500) {//�߰��� �Ÿ��� ���� ������ ������ ������ ���� ��
						if (dist < ebuff.hitrange) {// �߰��� �Ÿ��� ���� ������ ������ ������ ����
													// �� -
							// hitrange : �� ĳ���͸��� �׸��� ����
							// ���������Ǵ� ������ �ٸ���
							ebuff.life -= buff.power;
							if (ebuff.life <= 0) {// �� ������ ����
								Item tem;
								removeObject(enemies, enemies.get(j));
								// enemies.remove(j);// �� ĳ���� �Ұ�
								expl = new Effect(0, ebuff.pos.x, buff.pos.y - Effect.EFHEIGHT / 2, 0);
								addObject(effects, expl);
								// effects.add(expl);// ���� ����Ʈ �߰�
								// Item tem=new Item(ebuff.pos.x, buff.pos.y,
								// RAND(1,(level+1)*20)/((level+1)*20));//���� �����
								// �ִ밪�� ���� �����Ǵ� �������� 1�� �ȴ�
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
							players[buff.from].addScore(1);// ���� �߰�
							removeObject(bullets, bullets.get(i));
							// bullets.remove(i);// �Ѿ� �Ұ�
							break;// �Ѿ��� �ҰŵǾ����Ƿ� ���� �ƿ�
						}
					}
				} else {// ���� �� �Ѿ��� �÷��̾�� ���� ����
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
						} else {// �ǵ尡 ���� ���
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
		// ���� ���� �������� �߰�
		if (isBoss) {
			// ������ �����Ǿ� �ִ� ��Ȳ�� ó��
			if (level > 1) {// ���� ������ 2 �̻��̸�, ������ ���߿� ���� ĳ���͵��� ������� ���´�
				// ������ ���� �ó�����
				// : ����ī��Ʈ(gamecnt) 0~200 : ������ ����
				// : ����ī��Ʈ(gamecnt) 801~1000 : ������ 60ī��Ʈ ������ ����
				// : ����ī��Ʈ(gamecnt) 1601~2199 : ������ 30ī���� ������ ����
				if (800 < gameCnt && gameCnt < 1000) {// ������ �����ϰ� �ð��� �� ������ ����
					// ĳ���͵��� ��������
					// �����Ѵ�
					if (gameCnt % 60 == 0) {
						newy = GamePanel.RAND(30, SMFrame.SCREEN_HEIGHT - 50) * 100;
						if (newy < 24000)
							mode = 0;
						else
							mode = 1;
						Enemy en = new Enemy(gamePanel, 0, SMFrame.SCREEN_WIDTH * 100, newy, 0, mode, level);
						enemies.add(en);
					}
				} else if (1600 < gameCnt && gameCnt < 2200) {// �������� �Ĺݺο� ���鼭
					// ���� ��������� ������
					// �ż�����
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
			if (gameCnt > 2210) {// ������ Ÿ�� �ƿ����� �������� �����Ѵ�
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
				// ������ ������ �÷��ִ� �κп���, ������ �÷��ָ鼭 ���� ĳ���͸� �����Ų��
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
				if (dist < 1000) {// ������ ȹ��
					switch (buff.kind) {
					case 0:// �Ϲ� ����
						players[j].addScore(100);
						break;
					case 1:// �ǵ�
						players[j].setShield(5);
						break;
					case 2:// ���� ������
							// Enemy ebuff;
							// Effect expl;

						// �� ���� ȿ��
						Effect expl;
						for (int k = 0; k < enemies.size(); k++) {
							Enemy ebuff = (Enemy) (enemies.elementAt(k));
							if (ebuff == null)
								continue;// ���� �ش� �ε����� �� ĳ���Ͱ� �����Ǿ����� ���� ��츦 ���
							if (ebuff.kind == 1) {// �ش� �ε����� �Ҵ�� �� ĳ���Ͱ� ���� ĳ������
								// ���� ���꿡 �ش����� �ʰ� HP 10 ����
								// ���δ�. 1 ���϶�� 1.
								players[j].addScore(100);
								ebuff.life = ebuff.life - 10*level;
								if (ebuff.life <= 1)
									ebuff.life = 1;
								continue;
							}
							expl = new Effect(0, buff.pos.x, buff.pos.y, 0);
							if (ebuff.life-- <= 0) {// �� ������ ����
								if (ebuff.kind == 1) {
									if (gameCnt < 2100)
										gameCnt = 2100;
								}
								removeObject(enemies, enemies.get(j));
								// enemies.remove(j);// �� ĳ���� �Ұ�
								expl = new Effect(0, ebuff.pos.x, buff.pos.y, 0);
								addObject(effects, expl);
								// effects.add(expl);// ���� ����Ʈ �߰�
								// Item tem=new Item(ebuff.pos.x, buff.pos.y,
								// RAND(1,(level+1)*20)/((level+1)*20));//���� �����
								// �ִ밪�� ���� �����Ǵ� �������� 1�� �ȴ�
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
							// score++;// ���� �߰�
							expl = new Effect(0, ebuff.pos.x, ebuff.pos.y, 0);
							addObject(effects, expl);
							// effects.add(expl);// ���� ����Ʈ �߰�
							ebuff.pos.x = -10000;// ���� ó������ �Ұŵ� �� �ֵ���
							players[j].addScore(50);
							removeObject(enemies, ebuff);
							// enemies.remove(ebuff);//�� ĳ���� �Ұ�
						}

						// �� �Ѿ� ���� �Ұ�
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
		// System.out.println(instance.getClass().getName() + " ����, ���� : " +
		// vec.size());
	}

	public int GetDistance(int x1, int y1, int x2, int y2) {
		return Math.abs((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	public int getLevel() {
		return this.level;
	}
}