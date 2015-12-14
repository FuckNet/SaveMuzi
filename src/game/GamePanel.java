package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;
import java.util.Vector;

import javax.swing.JComponent;

import listener.SMKeyListener;
import main.SMFrame;
import network.SMNet;
import network.SMQueue;
import superPanel.ReceiveJPanel;

public class GamePanel extends ReceiveJPanel {

	private SMFrame smFrame;
	// private GameScreen gameScreen;
	public SMThread mainWork;
	
	private GameScreen gameScreen;
	private SMNet smNet;
	private Player p[];
	private int maxPlayerNum = 1;
	private SMQueue smQueue;
	private Vector<Object> bullets;

	public static Random rnd;

	// 1 2 3 4
	public static enum Status {
		GameStart, PlayScreen, Pause, GameOver
	};

	public GamePanel(SMFrame smFrame) {
		this.smFrame = smFrame;

		setLayout(null);

		init();

		gameScreen = new GameScreen(this);
		gameScreen.init();
		gameScreen.setSize(SMFrame.SCREEN_WIDTH, SMFrame.SCREEN_HEIGHT);
		gameScreen.setLocation(0, 0);
		add(gameScreen);
		
		smNet = smFrame.getSMNet();

	}

	public void init() {
		bullets = new Vector<Object>();
	}
	
	public SMNet getSMNet() {
		return smNet;
	}

	public void addBullet(Bullet bullet) {
		//add(bullet);
		bullets.add(bullet);
	}

	public void setThread(SMThread smThread) {
		this.mainWork = smThread;
	}

	public Player[] getPlayer() {
		return this.p;
	}

	public Vector<Object> getBullets() {
		return this.bullets;
	}

	public synchronized static int RAND(int startnum, int endnum) {
		int a, b;
		if (startnum < endnum)
			b = endnum - startnum; // b는 실제 난수 발생 폭
		else
			b = startnum - endnum;
		a = Math.abs(rnd.nextInt() % (b + 1));
		return (a + startnum);
	}

	public void setMaxPlayerNum(int num) {
		this.maxPlayerNum = num;
		Enemy.setMaxPlayerNum(num);
	}

	@Override
	public void receiveMSG(String msg) {
		String splitSlash[];
		splitSlash = msg.split("/");
		int count = splitSlash.length;
		for (int i = 0; i < count; i++) {
			if(splitSlash[i].substring(0, 3).equals("LOC")) {
				String splitSpace[];
				int playerNum;
				int x, y;
				splitSpace = splitSlash[i].split(" ");
				playerNum = Integer.parseInt(splitSpace[1]);
				x = Integer.parseInt(splitSpace[2]);
				y = Integer.parseInt(splitSpace[3]);
				p[playerNum].setX(x);
				p[playerNum].setY(y);
			}
			else
				smQueue.addMSG(splitSlash[i]);
		}
	}

	public void gameStart() {
		smQueue = SMQueue.getSMQueue();
		p = new Player[maxPlayerNum + 1];
		for (int i = 1; i <= maxPlayerNum; i++) {
			p[i] = new Player(this);
			p[i].setY(smFrame.getHeight() * i / (maxPlayerNum + 1));
		}

		Enemy.setPlayers(p);
		gameScreen.setPlayers(p);

		mainWork = new SMThread();

		mainWork.setGamePanel(this);
		mainWork.setGameScreen(gameScreen);

		mainWork.start();

		addKeyListener(new SMKeyListener(smNet));
		requestFocus();
	}

}
