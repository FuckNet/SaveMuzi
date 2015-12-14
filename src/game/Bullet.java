package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JLabel;

// Bullet
public class Bullet extends Object{

	private static final int BWIDTH = 15, BHEIGHT = 15;
	// 게임에 등장하는 총알을 처리하기 위한 클래스
	// 메모리 효율을 위해서는 총알에 관한 최소한의 정보만 담는 것이 좋지만, 처리 샘플을 위해 간단한 자체 처리 루틴을 포함한다.
	Point dis;// 총알의 표시 좌표. 실제 좌표보다 *100 상태이다.
	Point pos;// 총알의 계산 좌표. 실제 좌표보다 *100 상태이다.
	Point _pos;// 총알의 직전 좌표
	Point center;
	int degree;// 총알의 진행 방향 (각도)
	// 총알의 진행 방향은 x, y 증가량으로도 표시 가능하다. 하지만 그 경우 정밀한 탄막 구현이 어려워진다.
	int speed;// 총알의 이동 속도
	int imgIndex;// 총알의 이미지 번호
	int from;// 총알을 누가 발사했는가
	int power; // 총알의 파워
	static Image bulletImg[] = new Image[3];
	static Image eBulletImg[] = new Image[1];

	public static void bulletInit() {
		for (int i = 0; i < bulletImg.length; i++) {
			bulletImg[i] = Toolkit.getDefaultToolkit().getImage("res/game/bullet_" + i + ".png");
		}
		for (int i = 0; i < eBulletImg.length; i++) {
			eBulletImg[i] = Toolkit.getDefaultToolkit().getImage("res/game/ebullet_" + i + ".png");
		}
	}

	public Bullet(int x, int y, int img_num, int from, int degree, int speed, int power) {
		pos = new Point(x, y);
		dis = new Point(x / 100, y / 100);
		_pos = new Point(x, y);
		center = new Point(dis.x + BWIDTH/2, dis.y + BHEIGHT/2);
		this.imgIndex = img_num;
		this.from = from;
		this.degree = degree;
		this.speed = speed;
		this.power = power;
	}

	public void move() {
		_pos = pos;// 이전 좌표 보존
		pos.x -= (speed * Math.sin(Math.toRadians(degree)) * 100);
		pos.y -= (speed * Math.cos(Math.toRadians(degree)) * 100);
		dis.x = pos.x / 100;
		dis.y = pos.y / 100;
		center.x = dis.x + BWIDTH / 2;
		center.y = dis.y + BHEIGHT / 2;
		// if(pos.x<0||pos.y>gScreenWidth*100||pos.y<0||pos.y>gScreenHeight*100)
		// ebullet[i].pic=255;
	}

	public void paint(Graphics g) {
		if(from == 0)
			g.drawImage(bulletImg[imgIndex], dis.x, dis.y, BWIDTH, BHEIGHT, null);
		else
			g.drawImage(eBulletImg[imgIndex], dis.x, dis.y, BWIDTH, BHEIGHT, null);
		// g.drawImage(bulletImg[imgIndex], 0, 0, BWIDTH, BHEIGHT, null);
	}
}
