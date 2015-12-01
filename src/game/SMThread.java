package game;

import java.awt.event.KeyEvent;

import game.GamePanel.Status;
import listener.SMKeyListener;

public class SMThread extends Thread {
	
	// private GameScreen gameScreen;
	private Status status;
	private GamePanel gamePanel;

	private Player players[];

	// 게임 상태 변수
	private int cnt; // 그림을 그리는데 사용하는 int 변수
	private int gameCnt; // 게임 도중에 사용되는 이벤트 처리성 int 변수

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
	}

	private void gameInit() {
		cnt = 0;
		gameCnt = 0;
		status = Status.GameStart;
	}

	public void run() {
		long preTime; // 루프 간격을 조절하기 위한 시간 체크 값
		int delay = 17; // 루프 딜레이, 1/1000초 단위. > 17/1000초 = 58(프레임/초)
		try {
			while (true) {
				preTime = System.currentTimeMillis();

				gamePanel.repaint();
				process();
				//keyProcess();

				//Thread.sleep(100);

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

	private void process() {
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

	private void keyProcess() {
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

}