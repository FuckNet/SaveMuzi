package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

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
	private Image[] character;
	private Image img;
	
	private boolean state[];
	
	public Player() {
		initData();
		setSize(PWIDTH, PHEIGHT);
		state = new boolean[999];
		for(int i = 0; i < state.length; i++) {
			state[i] = false;
		}
	}
	
	public void initData() {
		score=0;
		x = 0;
		y = 0;
		speed=4;
		degree=-1;
		mode=1;
		imgIndex=2;
		cnt=0;
		life=3;
		inv = false;
		character = new Image[9];
		//이미지 저장
		for(int i = 0; i < 9; i++) {
			//if(i < 10)
				character[i] = Toolkit.getDefaultToolkit().getImage("res/character/c1_0" + i + ".png");
			
		}
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(character[imgIndex], 0, 0, PWIDTH, PHEIGHT, null);
	}
	
	public void setState(int index, boolean bool) {
		state[index] = bool;
	}
	
	public void process() {
		imgIndex = 2;
		if(state[KeyEvent.VK_UP] == true)
			y -= speed * 100;
		if(state[KeyEvent.VK_DOWN] == true)
			y += speed * 100;
		if(state[KeyEvent.VK_RIGHT] == true)
			x += speed * 100;
		if(state[KeyEvent.VK_LEFT] == true) {
			x -= speed * 100;
			imgIndex = 4;
		}
		
		switch(mode){
		case 1:
			x+=200;
			if(x>13000) mode=2;
			break;
		case 0:
			if(cnt--==0) {
				mode=2;
				imgIndex=0;
			}
		case 2:
			setLocation(x/100, y/100);
			break;
		}
		if(x<0) x=0;
		if(x>102400-7000) x=102400-7000;
		if(y<3000) y=3000;
		if(y>72000-7000) y=72000-7000;
	}
	
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public void setImgIndex(int imgIndex) {
		this.imgIndex = imgIndex;
	}
	
	public void setX(int x) {
		this.x = x * 100;
	}
	
	public void setY(int y) {
		this.y = y * 100;
	}
	
	public int getX() {
		return this.x/100;
	}
	
	public int getY() {
		return this.y/100;
	}
	
}
