package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JLabel;

public class Player extends JLabel{
	
	private static final int PWIDTH = 70, PHEIGHT = 70;
	
	private int score;
	private int x;
	private int y;
	private int speed;
	private int degree;
	private int mode;
	private int imgIndex;
	private int cnt;
	private int life;
	private boolean inv;
	private boolean isShooting;
	private Image[] character;
	private Image img;
	
	public Player() {
		initData();
		setSize(PWIDTH, PHEIGHT);
		
	}
	
	public void initData() {
		score=0;
		x=0;
		y=23000;
		speed=4;
		degree=-1;
		mode=1;
		imgIndex=2;
		cnt=0;
		life=3;
		inv = false;
		isShooting = false;
		character = new Image[9];
		//이미지 저장
		for(int i = 0; i < 9; i++) {
			//if(i < 10)
				character[i] = Toolkit.getDefaultToolkit().getImage("res/character/c1_0" + i + ".png");
			
		}
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		System.out.println("hi");
		g.drawImage(character[imgIndex], 0, 0, PWIDTH, PHEIGHT, null);
	}
	
	public void process() {
		switch(mode){
		case 1:
			x+=200;
			if(x>20000) mode=2;
			break;
		case 0:
			if(cnt--==0) {
				mode=2;
				imgIndex=0;
			}
		case 2:
			if(degree!=-1&&inv) degree=(degree+180)%360;
			if(degree>-1) {
				x-=(speed*Math.sin(Math.toRadians(degree))*100);
				y-=(speed*Math.cos(Math.toRadians(degree))*100);
			}
			if(imgIndex==6) {
				x-=20;
//				if(cnt%4==0||shoot){
//					isShooting=false;
//					shoot=new Bullet(x+2500, y+1500, 0, 0, RAND(245,265), 8);
//					bullets.add(shoot);
//					shoot=new Bullet(x+2500, y+1500, 0, 0, RAND(268,272), 9);
//					bullets.add(shoot);
//					shoot=new Bullet(x+2500, y+1500, 0, 0, RAND(275,295), 8);
//					bullets.add(shoot);
//				}
				//8myy+=70;
			}
			break;
		case 3:
			//keybuff=0;
			imgIndex=8;
			if(cnt--==0) {
				mode=0;
				cnt=50;
			}
			break;
		}
		if(x<2000) x=2000;
		if(x>62000) x=62000;
		if(y<3000) y=3000;
		if(y>45000) y=45000;
	}
	
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public void setImgIndex(int imgIndex) {
		this.imgIndex = imgIndex;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
}
