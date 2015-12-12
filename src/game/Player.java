package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;

import main.SMFrame;

public class Player extends JLabel{
	
	private static final int PWIDTH = 70, PHEIGHT = 70;
	
	private GamePanel gamePanel;
	private int score;
	private int x;
	private int y;
	private int speed;
	private int degree;
	private int mode;
	private int imgIndex;
	private int cnt;
	private int life;
	private int shieldPoint;
	private int power;
	private Vector<JComponent> bullets;
	private boolean isShoot;
	private boolean inv;
	private Image[] character;
	private Image img;
	
	private boolean state[];
	
	public Player(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
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
		life=300;
		bullets = gamePanel.getBullets();
		isShoot = false;
		shieldPoint = 0;
		power = 1;
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
		if(state[KeyEvent.VK_SPACE] == true) {
			imgIndex = 6;
			isShoot = true;
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
			if(imgIndex == 6) {
				if(cnt % 4 == 0 || isShoot) {
					isShoot = false;
					Bullet shoot = new Bullet(x+5000, y+4000, 0, 0, 270, 8, power);
					gamePanel.add(shoot);
					bullets.add(shoot);
				}
			}
			setLocation(x/100, y/100);
			break;
		}
		if(x<0) x=0;
		if(x>SMFrame.SCREEN_WIDTH * 100-7000) x=SMFrame.SCREEN_WIDTH * 100-7000;
		if(y<3000) y=3000;
		if(y>SMFrame.SCREEN_HEIGHT * 100-7000) y=SMFrame.SCREEN_HEIGHT * 100-7000;
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
	public void setMode(int mode) {
		this.mode = mode;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public void setShield(int shieldPoint) {
		this.shieldPoint = shieldPoint;
	}
	
	
	
	public int getX() {
		return this.x/100;
	}
	public int getY() {
		return this.y/100;
	}
	public int getMode() {
		return this.mode;
	}
	public int getShield() {
		return this.shieldPoint;
	}
	public int getLife() {
		return this.life;
	}
	
}
