package game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JPanel;

import listener.SMKeyListener;
import main.SMFrame;
import network.SMNet;

public class GamePanel extends JPanel {
	
	private SMFrame smFrame;
	//private GameScreen gameScreen;
	private SMThread mainWork; 
	
	private SMNet smNet;
	private Player p[];
	private Image dblBuff;
	private Graphics gc;
	private Image background;
	
	private static Random rnd = new Random();
	
	//                             1          2         3       4
	public static enum Status {GameStart, PlayScreen, Pause, GameOver};
	
	public GamePanel(SMFrame smFrame) {
		this.smFrame = smFrame;
		
		setLayout(null);
		
		smNet = smFrame.getSMNet();
		
//		gameScreen = new GameScreen(this);
//		gameScreen.setBounds(0, 0, smFrame.getWidth(), smFrame.getHeight());
//		add(gameScreen);
		
		background = Toolkit.getDefaultToolkit().getImage("res/background/backgroundLogin.png");
		
		p = new Player[2];
		p[0] = new Player();
		p[0].setY(smFrame.getHeight() / 3);
		add(p[0]);

		p[1] = new Player();
		p[1].setY(smFrame.getHeight()*2 / 3);
		add(p[1]);
		
		
		
		mainWork = new SMThread();
		
		mainWork.setGamePanel(this);
		
		//gameScreen.setThread(mainWork);
		//mainWork.setGameScreen(gameScreen);
		
		mainWork.start();
		
		addKeyListener(new SMKeyListener(new SMNet()));
		requestFocus();
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
	
	public static int RAND(int startnum, int endnum) //랜덤범위(startnum부터 ramdom까지), 랜덤값이 적용될 변수.
	{
		int a, b;
		if(startnum<endnum)
			b = endnum - startnum; //b는 실제 난수 발생 폭
		else
			b = startnum - endnum;
		a = Math.abs(rnd.nextInt()%(b+1));
		return (a+startnum);
	}

}
