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

	// 게임 상태 변수
	private int cnt; // 그림을 그리는데 사용하는 int 변수
	public static int gameCnt; // 게임 도중에 사용되는 이벤트 처리성 int 변수

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
		long preTime; // 루프 간격을 조절하기 위한 시간 체크 값
		int delay = 17; // 루프 딜레이, 1/1000초 단위. > 17/1000초 = 58(프레임/초)
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
				// 게임 루프를 처리하는데 걸린 시간을 체크해서 딜레이값에서 차감하여 딜레이를 일정하게 유지한다.
				// 루프 실행 시간이 딜레이 시간보다 크다면 게임 속도가 느려지게 된다.
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
		case GameStart:// 스타트
			processPlayer();
			// if(mymode==2) status=2;
			break;
		case PlayScreen:// 게임화면
			processPlayer();
			// process_ENEMY();
			// process_BULLET();
			// process_EFFECT();
			// process_GAMEFLOW();
			// process_ITEM();
			break;
		case GameOver:// 게임오버
			// process_ENEMY();
			// process_BULLET();
			// process_GAMEFLOW();
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
				bullets.remove(i);//화면 밖으로 나가면 총알 제거
				continue;
			}
			if(buff.from==0) {//플레이어가 쏜 총알이 적에게 명중 판정
				for(j=0;j<enemies.size();j++){
					ebuff=(Enemy)(enemies.elementAt(j));
					dist=GetDistance(buff.dis.x,buff.dis.y, ebuff.dis.x,ebuff.dis.y);
					//if(dist<1500) {//중간점 거리가 명중 판정이 가능한 범위에 왔을 때
					if(dist<ebuff.hitrange) {//중간점 거리가 명중 판정이 가능한 범위에 왔을 때 - hitrange : 적 캐릭터마다 그림에 따라 명중판정되는 범위가 다르다
						if(ebuff.life--<=0){//적 라이프 감소
							if(ebuff.kind==1){
								if(gameCnt<2100) gameCnt=2100;
							}
							enemies.remove(j);//적 캐릭터 소거
							expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
							effects.add(expl);//폭발 이펙트 추가
							//Item tem=new Item(ebuff.pos.x, buff.pos.y, RAND(1,(level+1)*20)/((level+1)*20));//난수 결과가 최대값일 때만 생성되는 아이템이 1이 된다
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
						score++;//점수 추가
						bullets.remove(i);//총알 소거
						break;//총알이 소거되었으므로 루프 아웃
					}
				}
			} else {//적이 쏜 총알이 플레이어에게 명중 판정
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
					} else {//실드가 있을 경우
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