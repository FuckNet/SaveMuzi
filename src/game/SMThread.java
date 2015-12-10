package game;

import java.util.Vector;

import game.GamePanel.Status;
import main.SMFrame;
import network.SMQueue;

public class SMThread extends Thread {
	
	// private GameScreen gameScreen;
	private Status status;
	private GamePanel gamePanel;
	private SMQueue smQueue;
	private Player players[];
	private Vector<Bullet> bullets;
	private Vector<Enemy> enemies;
	private Vector<Effect> effects;
	private Vector<Item> items;
	private int score;

	// ���� ���� ����
	private int cnt; // �׸��� �׸��µ� ����ϴ� int ����
	public static int gameCnt; // ���� ���߿� ���Ǵ� �̺�Ʈ ó���� int ����

	public SMThread() {
		gameInit();
	}

	// public void setGameScreen(GameScreen gameScreen) {
	// this.gameScreen = gameScreen;
	//
	// }

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		players = gamePanel.getPlayer();
		bullets = gamePanel.getBullets();
	}

	private void gameInit() {
		cnt = 0;
		gameCnt = 0;
		score = 0;
		status = Status.GameStart;
		smQueue = SMQueue.getSMQueue();
	}

	public void run() {
		long preTime; // ���� ������ �����ϱ� ���� �ð� üũ ��
		int delay = 17; // ���� ������, 1/1000�� ����. > 17/1000�� = 58(������/��)
		try {
			while (true) {
				preTime = System.currentTimeMillis();

				//Thread.sleep(50);
				//gamePanel.repaint();
				process();
				//keyProcess();
				
//				for(int i = 0; i < 100; i++) {
//					keyProcess();
//					Thread.sleep(1);
//				}

				//Thread.sleep(100);
				//System.out.println(preTime);
				if (System.currentTimeMillis() - preTime < delay)
					Thread.sleep(delay - System.currentTimeMillis() + preTime);
				// ���� ������ ó���ϴµ� �ɸ� �ð��� üũ�ؼ� �����̰����� �����Ͽ� �����̸� �����ϰ� �����Ѵ�.
				// ���� ���� �ð��� ������ �ð����� ũ�ٸ� ���� �ӵ��� �������� �ȴ�.
				gamePanel.repaint();
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
			// if(mymode==2) status=2;
			break;
		case PlayScreen:// ����ȭ��
			processPlayer();
			// process_ENEMY();
			// process_BULLET();
			// process_EFFECT();
			// process_GAMEFLOW();
			// process_ITEM();
			break;
		case GameOver:// ���ӿ���
			// process_ENEMY();
			// process_BULLET();
			// process_GAMEFLOW();
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
		if(msg.equals(null) || msg.equals(""))
			return;
		
		String splitMsg[] = msg.split(" ");
		int playerNum;
		for(int i = 0; i < splitMsg.length; i++) {
			playerNum = Integer.parseInt(splitMsg[0]);
			players[playerNum].setState(Integer.parseInt(splitMsg[2]), splitMsg[1].equals("P"));
		}
		
//		if (SMKeyListener.getKeyState(KeyEvent.VK_DOWN) && SMKeyListener.getKeyState(KeyEvent.VK_LEFT)) {
//			players[0].setY(players[0].getY() + 10);
//			players[0].setX(players[0].getX() - 10);
//			players[0].setDegree(135);
//			players[0].setImgIndex(4);
//		} if (SMKeyListener.getKeyState(KeyEvent.VK_DOWN) && SMKeyListener.getKeyState(KeyEvent.VK_RIGHT)) {
//			players[0].setY(players[0].getY() + 10);
//			players[0].setX(players[0].getX() + 10);
//			players[0].setDegree(225);
//			players[0].setImgIndex(2);
//		} if (SMKeyListener.getKeyState(KeyEvent.VK_UP) && SMKeyListener.getKeyState(KeyEvent.VK_LEFT)) {
//			players[0].setY(players[0].getY() - 10);
//			players[0].setX(players[0].getX() - 10);
//			players[0].setDegree(45);
//			players[0].setImgIndex(4);
//		} if (SMKeyListener.getKeyState(KeyEvent.VK_UP) && SMKeyListener.getKeyState(KeyEvent.VK_RIGHT)) {
//			players[0].setY(players[0].getY() - 10);
//			players[0].setX(players[0].getX() + 10);
//			players[0].setDegree(315);
//			players[0].setImgIndex(2);
//		} else {
//			if (SMKeyListener.getKeyState(KeyEvent.VK_DOWN)) {
//				players[0].setY(players[0].getY() + 10);
//				players[0].setDegree(180);
//				players[0].setImgIndex(2);
//			}
//			if (SMKeyListener.getKeyState(KeyEvent.VK_UP)) {
//				players[0].setY(players[0].getY() - 10);
//				players[0].setDegree(0);
//				players[0].setImgIndex(2);
//			}
//			if (SMKeyListener.getKeyState(KeyEvent.VK_LEFT)) {
//				players[0].setX(players[0].getX() - 10);
//				players[0].setDegree(90);
//				players[0].setImgIndex(4);
//			}
//			if (SMKeyListener.getKeyState(KeyEvent.VK_RIGHT)) {
//				players[0].setX(players[0].getX() + 10);
//				players[0].setDegree(270);
//				players[0].setImgIndex(2);
//			}
//		}
		
		//players[0].setState(KeyEvent.VK_UP, SMKeyListener.getKeyState(KeyEvent.VK_UP));
		//players[0].setState(KeyEvent.VK_DOWN, SMKeyListener.getKeyState(KeyEvent.VK_DOWN));
		//players[0].setState(KeyEvent.VK_RIGHT, SMKeyListener.getKeyState(KeyEvent.VK_RIGHT));
		//players[0].setState(KeyEvent.VK_LEFT, SMKeyListener.getKeyState(KeyEvent.VK_LEFT));
		
	}
	public void process_BULLET(){
		Bullet buff;
		Enemy ebuff;
		Effect expl;
		int i,j, dist;
		for(i=0;i<bullets.size();i++){
			buff=(Bullet)(bullets.elementAt(i));
			buff.move();
			if(buff.dis.x<10||buff.dis.x>SMFrame.SCREEN_WIDTH+10||buff.dis.y<10||buff.dis.y>SMFrame.SCREEN_HEIGHT+10) {
				bullets.remove(i);//ȭ�� ������ ������ �Ѿ� ����
				continue;
			}
			if(buff.from==0) {//�÷��̾ �� �Ѿ��� ������ ���� ����
				for(j=0;j<enemies.size();j++){
					ebuff=(Enemy)(enemies.elementAt(j));
					dist=GetDistance(buff.dis.x,buff.dis.y, ebuff.dis.x,ebuff.dis.y);
					//if(dist<1500) {//�߰��� �Ÿ��� ���� ������ ������ ������ ���� ��
					if(dist<ebuff.hitrange) {//�߰��� �Ÿ��� ���� ������ ������ ������ ���� �� - hitrange : �� ĳ���͸��� �׸��� ���� ���������Ǵ� ������ �ٸ���
						if(ebuff.life--<=0){//�� ������ ����
							if(ebuff.kind==1){
								if(gameCnt<2100) gameCnt=2100;
							}
							enemies.remove(j);//�� ĳ���� �Ұ�
							expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
							effects.add(expl);//���� ����Ʈ �߰�
							//Item tem=new Item(ebuff.pos.x, buff.pos.y, RAND(1,(level+1)*20)/((level+1)*20));//���� ����� �ִ밪�� ���� �����Ǵ� �������� 1�� �ȴ�
							int itemKind=GamePanel.RAND(1,100);
							Item tem;
							if(itemKind<=70)
								tem=new Item(ebuff.pos.x, buff.pos.y,0);
							else if(itemKind<=95)
								tem=new Item(ebuff.pos.x, buff.pos.y,2);
							else
								tem=new Item(ebuff.pos.x, buff.pos.y,1);
							items.add(tem);
						}
						//expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
						expl=new Effect(0, buff.pos.x, buff.pos.y, 0);
						effects.add(expl);
						score++;//���� �߰�
						bullets.remove(i);//�Ѿ� �Ұ�
						break;//�Ѿ��� �ҰŵǾ����Ƿ� ���� �ƿ�
					}
				}
			} else {//���� �� �Ѿ��� �÷��̾�� ���� ����
				for(int k = 1; k <= players.length; k++) {
				if(players[k].getMode()!=2) continue;
				dist=GetDistance(players[k].getX(),players[k].getY(), buff.dis.x,buff.dis.y);
				if(dist<500) {
					if(players[k].getShield()==0){
						players[k].setMode(3);
						players[k].setCnt(30);
						bullets.remove(i);
						expl=new Effect(0, players[k].getX() * 100-2000, players[k].getX() * 100, 0);
						effects.add(expl);
						int temp = players[k].getLife() - 1;
						players[k].setLife(temp);
						if(temp<=0) {
							status=Status.GameOver;
							gameCnt=0;
						}
					} else {//�ǵ尡 ���� ���
						players[k].setShield(players[k].getShield() - 1);
						bullets.remove(i);
					}
				}
				}
			}
		}
	}
	
	public int GetDistance(int x1,int y1,int x2,int y2){
		return Math.abs((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
	}
}