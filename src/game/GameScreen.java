package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import main.SMFrame;

//GameScreen
public class GameScreen extends Canvas {

	private Player[] players;
	private Vector<Object> bullets;
	private Vector<Object> enemies;
	private Vector<Object> effects;
	private Vector<Object> items;

	// private SMNet smNet;
	private GamePanel gamePanel;
	private SMThread smThread;

	private Image dblbuff;
	private Graphics gc;
	private Image background = Toolkit.getDefaultToolkit().getImage("res/background/backgroundGame.png");
	private Image bg_f = Toolkit.getDefaultToolkit().getImage("res/background/bg_f.png");
	private Image cloud[];
	private int scrSpeed;	// ��ũ�� �ӵ�
	
	private int step;
	
	int v[] = {-2,-1,0,1,2,1,0,-1};

	public GameScreen(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gamePanel.requestFocus();
			}
		});
	}

	public void init() {
		step = 0;
		scrSpeed = 16;
		cloud = new Image[4];
		for(int i = 0; i < cloud.length; i++)
			cloud[i] = Toolkit.getDefaultToolkit().getImage("res/background/cloud" + i + ".png");
		this.smThread = gamePanel.getSMThread();
	}

	public void paint(Graphics g) {
		if (gc == null) {
			dblbuff = createImage(SMFrame.SCREEN_WIDTH, SMFrame.SCREEN_HEIGHT);
			if (dblbuff == null)
				System.out.println("������ũ�� ���� ���� ����");
			else
				gc = dblbuff.getGraphics();// ������ũ�� ���ۿ� �׸��� ���� �׷��� ���ؽ�Ʈ ȹ��
			return;
		}
		update(g);
	}

	public void update(Graphics g) {// ȭ�� ���ڰŸ��� ���̱� ����, paint���� ȭ���� �ٷ� ��ȭ���� �ʰ�
									// update �޼ҵ带 ȣ���ϰ� �Ѵ�.
		// cnt=main.cnt;
		// gamecnt=main.gamecnt;
		if (gc == null)
			return;
		dblpaint();// ������ũ�� ���ۿ� �׸���
		g.drawImage(dblbuff, 0, 0, this);// ������ũ�� ���۸� ����ȭ�鿡 �׸���.
	}

	public void dblpaint() {
		// ��� �׸���
		gc.drawImage(background, 0, 0, SMFrame.SCREEN_WIDTH, SMFrame.SCREEN_HEIGHT, this);
		// UI �׸���
		drawUI();
		drawMoveBG();

		// ������Ʈ�� �׸���
		for (int i = 1; i < players.length; i++)
			players[i].paint(gc);
		for (int i = 0; i < bullets.size(); i++)
			bullets.elementAt(i).paint(gc);
		for (int i = 0; i < enemies.size(); i++)
			enemies.elementAt(i).paint(gc);
		for (int i = 0; i < effects.size(); i++)
			effects.elementAt(i).paint(gc);
		for (int i = 0; i < items.size(); i++)
			items.elementAt(i).paint(gc);
	}

	public void drawMoveBG(){
		int i;

		for(i=0;i<18;i++) gc.drawImage(cloud[3], i*64-((smThread.cnt/2)%128), SMFrame.SCREEN_HEIGHT - 110, this);
		for(i=-1;i<20;i++) gc.drawImage(cloud[2], i*64-(smThread.cnt%128)*2, SMFrame.SCREEN_HEIGHT - 85, this);
		if(bg_f.getWidth(this) < 0)
			return;
		step=(smThread.cnt%(bg_f.getWidth(this)/scrSpeed))*scrSpeed;
		gc.drawImage(bg_f,0-step,SMFrame.SCREEN_HEIGHT + 60 -bg_f.getHeight(this)+v[(smThread.cnt/20)%8]*2,this);
		//System.out.println("���"+step);
		if(step>=bg_f.getWidth(this)-SMFrame.SCREEN_WIDTH) {
			gc.drawImage(bg_f,0-step+bg_f.getWidth(this),SMFrame.SCREEN_HEIGHT + 60 -bg_f.getHeight(this)+v[(smThread.cnt/20)%8]*2,this);
		}
		for(i=-1;i<18;i++) gc.drawImage(cloud[0], i*128-(smThread.cnt%128)*8, SMFrame.SCREEN_HEIGHT - 45, this);
	}
	
	public void drawUI() {
		String level = "LEVEL " + smThread.getLevel();
		gc.setColor(new Color(0xffffff));
		gc.setFont(new Font("SansSerif", Font.BOLD, 20));
		gc.drawString(level, 0, 20);
		for (int i = 1; i < players.length; i++) {
			String str1 = "   P" + i + " : SCORE " + players[i].getScore() + "   LIFE " + players[i].getLife();
			// String str1="SCORE "+main.score+" LIFE "+main.mylife;
			//gc.setColor(new Color(0));
			//gc.drawString(str1, 9, 40);
			//gc.drawString(str1, 11, 40);
			//gc.drawString(str1, 10, 39);
			//gc.drawString(str1, 10, 41);
			if(i < 4) {
				gc.setColor(new Color(0xffffff));
				gc.setFont(new Font("SansSerif", Font.BOLD, 20));
				gc.drawString(str1, 300 * (i-1) + 100, 20);
			}

			//gc.setColor(new Color(0));
			//gc.drawString(str2, 9, main.gScreenHeight - 10);
			//gc.drawString(str2, 11, main.gScreenHeight - 10);
			//gc.drawString(str2, 10, main.gScreenHeight - 11);
			//gc.drawString(str2, 10, main.gScreenHeight - 9);
			else {
				gc.setColor(new Color(0xffffff));
				gc.setFont(new Font("SansSerif", Font.BOLD, 20));
				gc.drawString(str1, 300 * (i-4) + 10, SMFrame.SCREEN_HEIGHT - 30);
			}
		}
	}

	public void setBullets(Vector<Object> bullets) {
		this.bullets = bullets;
	}

	public void setEnemies(Vector<Object> enemies) {
		this.enemies = enemies;
	}

	public void setEffects(Vector<Object> effects) {
		this.effects = effects;
	}

	public void setItems(Vector<Object> items) {
		this.items = items;
	}

	public void setPlayers(Player[] p) {
		this.players = p;
	}

}
