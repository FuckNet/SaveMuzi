package game;

import java.awt.event.KeyEvent;

import game.GamePanel.Status;

public class SMThread extends Thread {
	
	//private GameScreen gameScreen;
	private Status status;
	private GamePanel gamePanel;
	
	private Player players[];
	
	//���� ���� ����
	private int cnt;	// �׸��� �׸��µ� ����ϴ� int ����
	private int gameCnt;	// ���� ���߿� ���Ǵ� �̺�Ʈ ó���� int ����
	
	public SMThread() {
		gameInit();
		
	}
	
//	public void setGameScreen(GameScreen gameScreen) {
//		this.gameScreen = gameScreen;
//		
//	}
	
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
		long preTime;	// ���� ������ �����ϱ� ���� �ð� üũ ��
		int delay = 17;	// ���� ������, 1/1000�� ����. > 17/1000�� = 58(������/��)
		try {
			while(true) {
				preTime = System.currentTimeMillis();
				
				gamePanel.repaint();
				process();
				keyProcess();
				
				Thread.sleep(100);
				
				if(System.currentTimeMillis()-preTime<delay) Thread.sleep(delay-System.currentTimeMillis()+preTime);
				//���� ������ ó���ϴµ� �ɸ� �ð��� üũ�ؼ� �����̰����� �����Ͽ� �����̸� �����ϰ� �����Ѵ�.
				//���� ���� �ð��� ������ �ð����� ũ�ٸ� ���� �ӵ��� �������� �ȴ�.

				if(status!=Status.GameOver) cnt++;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void process() {
		switch(status){
		case GameStart://��ŸƮ
			processPlayer();
			//if(mymode==2) status=2;
			break;
		case PlayScreen://����ȭ��
			processPlayer();
			//process_ENEMY();
			//process_BULLET();
			//process_EFFECT();
			//process_GAMEFLOW();
			//process_ITEM();
			break;
		case GameOver://���ӿ���
			//process_ENEMY();
			//process_BULLET();
			//process_GAMEFLOW();
			break;
		case Pause://�Ͻ�����
			break;
		default:
			break;
		}
		if(status!=Status.GameOver) gameCnt++;
	}
	
	private void processPlayer() {
		for(Player p : players) {
			p.process();
		}
	}
	
	private void keyProcess() {
		if(SMKeyListener.getKeyState(KeyEvent.VK_S)) {
			players[0].setY(players[0].getY() + 3);
			players[0].setDegree(180);
			players[0].setImgIndex(2);
		}
		if(SMKeyListener.getKeyState(KeyEvent.VK_W)) {
			players[0].setY(players[0].getY() - 3);
			players[0].setDegree(0);
			players[0].setImgIndex(6);
		}
		if(SMKeyListener.getKeyState(KeyEvent.VK_A)) {
			players[0].setX(players[0].getX() - 3);
			players[0].setDegree(90);
			players[0].setImgIndex(6);
		}
		if(SMKeyListener.getKeyState(KeyEvent.VK_D)) {
			players[0].setX(players[0].getX() + 3);
			players[0].setDegree(270);
			players[0].setImgIndex(6);
		}
	}
	
}