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

	private SMNet smNet;
	private Player p[];
	private Image dblBuff;
	private Graphics gc;
	private Image background;
	private int maxPlayerNum = 1;
	private SMQueue smQueue;
	private Vector<JComponent> bullets;

	private static Random rnd = new Random();

	// 1 2 3 4
	public static enum Status {
		GameStart, PlayScreen, Pause, GameOver
	};

	public GamePanel(SMFrame smFrame) {
		this.smFrame = smFrame;

		setLayout(null);
		
		init();

		smNet = smFrame.getSMNet();

		// gameScreen = new GameScreen(this);
		// gameScreen.setBounds(0, 0, smFrame.getWidth(), smFrame.getHeight());
		// add(gameScreen);

		background = Toolkit.getDefaultToolkit().getImage("res/background/backgroundLogin.png");

	}
	
	public void init() {
		bullets = new Vector<JComponent>();
	}
	
	public void addBullet(Bullet bullet) {
		add(bullet);
		bullets.add(bullet);
	}

	public void setThread(SMThread smThread) {
		this.mainWork = smThread;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		dblBuff = createImage(smFrame.getWidth(), smFrame.getHeight());
		gc = dblBuff.getGraphics();
		gc.drawImage(background, 0, 0, smFrame.getWidth(), smFrame.getHeight(), this);
	}

	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(gc);
		g.drawImage(dblBuff, 0, 0, this);
	}

	public Player[] getPlayer() {
		return this.p;
	}
	public Vector<JComponent> getBullets() {
		return this.bullets;
	}
	public synchronized static int RAND(int startnum, int endnum)
	{
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
			smQueue.addMSG(splitSlash[i]);
			mainWork.keyProcess();
			mainWork.process();
			repaint();
		}
	}

	public void gameStart() {
		smQueue = SMQueue.getSMQueue();
		p = new Player[maxPlayerNum + 1];
		for (int i = 1; i <= maxPlayerNum; i++) {
			p[i] = new Player(this);
			p[i].setY(smFrame.getHeight() * i / (maxPlayerNum + 1));
			add(p[i]);
		}
		
		Enemy.setPlayers(p);

		mainWork = new SMThread();

		mainWork.setGamePanel(this);

		mainWork.start();

		addKeyListener(new SMKeyListener(smNet));
		requestFocus();
	}

}
