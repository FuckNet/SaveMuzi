package game;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;

import main.SMFrame;

public class GameScreen extends Canvas {
	
	private Player[] players;
	private Vector<Object> bullets;
	private Vector<Object> enemies;
	private Vector<Object> effects;
	private Vector<Object> items;
	
	private Image dblbuff;
	private Graphics gc;
	private Image background = Toolkit.getDefaultToolkit().getImage("res/background/backgroundLogin.png");
	
	public void init() {
		
	}
	
	public void paint(Graphics g){
		if(gc==null) {
			dblbuff=createImage(SMFrame.SCREEN_WIDTH, SMFrame.SCREEN_HEIGHT);//���� ���۸��� ������ũ�� ���� ����. ���� paint �Լ� ������ �� ��� �Ѵ�. �׷��� ������ null�� ��ȯ�ȴ�.
			if(dblbuff==null) System.out.println("������ũ�� ���� ���� ����");
			else gc=dblbuff.getGraphics();//������ũ�� ���ۿ� �׸��� ���� �׷��� ���ؽ�Ʈ ȹ��
			return;
		}
		update(g);
	}
	public void update(Graphics g){//ȭ�� ���ڰŸ��� ���̱� ����, paint���� ȭ���� �ٷ� ��ȭ���� �ʰ� update �޼ҵ带 ȣ���ϰ� �Ѵ�.
		//cnt=main.cnt;
		//gamecnt=main.gamecnt;
		if(gc==null) return;
		dblpaint();//������ũ�� ���ۿ� �׸���
		g.drawImage(dblbuff,0,0,this);//������ũ�� ���۸� ����ȭ�鿡 �׸���.
	}
	
	public void dblpaint() {
		// ��� �׸���
		gc.drawImage(background, 0, 0, SMFrame.SCREEN_WIDTH, SMFrame.SCREEN_HEIGHT, this);
		
		// ������Ʈ�� �׸���
		for(int i = 1; i < players.length; i++)
			players[i].paint(gc);
		for(int i = 0; i < bullets.size(); i++)
			bullets.elementAt(i).paint(gc);
		for(int i = 0; i < enemies.size(); i++)
			enemies.elementAt(i).paint(gc);
		for(int i = 0; i < effects.size(); i++)
			effects.elementAt(i).paint(gc);
		for(int i = 0; i < items.size(); i++)
			items.elementAt(i).paint(gc);
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
	public void setPlayers(Player[] p)	 {
		this.players = p;
	}
	
}
