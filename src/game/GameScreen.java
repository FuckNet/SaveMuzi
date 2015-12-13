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
			dblbuff=createImage(SMFrame.SCREEN_WIDTH, SMFrame.SCREEN_HEIGHT);//더블 버퍼링용 오프스크린 버퍼 생성. 필히 paint 함수 내에서 해 줘야 한다. 그렇지 않으면 null이 반환된다.
			if(dblbuff==null) System.out.println("오프스크린 버퍼 생성 실패");
			else gc=dblbuff.getGraphics();//오프스크린 버퍼에 그리기 위한 그래픽 컨텍스트 획득
			return;
		}
		update(g);
	}
	public void update(Graphics g){//화면 깜박거림을 줄이기 위해, paint에서 화면을 바로 묘화하지 않고 update 메소드를 호출하게 한다.
		//cnt=main.cnt;
		//gamecnt=main.gamecnt;
		if(gc==null) return;
		dblpaint();//오프스크린 버퍼에 그리기
		g.drawImage(dblbuff,0,0,this);//오프스크린 버퍼를 메인화면에 그린다.
	}
	
	public void dblpaint() {
		// 배경 그리기
		gc.drawImage(background, 0, 0, SMFrame.SCREEN_WIDTH, SMFrame.SCREEN_HEIGHT, this);
		
		// 오브젝트들 그리기
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
