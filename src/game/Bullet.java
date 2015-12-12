package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JLabel;

public class Bullet extends JLabel {

	private static final int BWIDTH = 15, BHEIGHT = 15;
	// ���ӿ� �����ϴ� �Ѿ��� ó���ϱ� ���� Ŭ����
	// �޸� ȿ���� ���ؼ��� �Ѿ˿� ���� �ּ����� ������ ��� ���� ������, ó�� ������ ���� ������ ��ü ó�� ��ƾ�� �����Ѵ�.
	Point dis;// �Ѿ��� ǥ�� ��ǥ. ���� ��ǥ���� *100 �����̴�.
	Point pos;// �Ѿ��� ��� ��ǥ. ���� ��ǥ���� *100 �����̴�.
	Point _pos;// �Ѿ��� ���� ��ǥ
	int degree;// �Ѿ��� ���� ���� (����)
	// �Ѿ��� ���� ������ x, y ���������ε� ǥ�� �����ϴ�. ������ �� ��� ������ ź�� ������ ���������.
	int speed;// �Ѿ��� �̵� �ӵ�
	int imgIndex;// �Ѿ��� �̹��� ��ȣ
	int from;// �Ѿ��� ���� �߻��ߴ°�
	int power; // �Ѿ��� �Ŀ�
	static Image bulletImg[] = new Image[4];

	public static void bulletInit() {
		for (int i = 0; i < bulletImg.length; i++) {
			bulletImg[i] = Toolkit.getDefaultToolkit().getImage("res/game/bullet_" + i + ".png");
		}
	}

	public Bullet(int x, int y, int img_num, int from, int degree, int speed, int power) {
		pos = new Point(x, y);
		dis = new Point(x / 100, y / 100);
		_pos = new Point(x, y);
		this.imgIndex = img_num;
		this.from = from;
		this.degree = degree;
		this.speed = speed;
		this.power = power;
		this.setSize(BWIDTH, BHEIGHT);
	}

	public void move() {
		_pos = pos;// ���� ��ǥ ����
		pos.x -= (speed * Math.sin(Math.toRadians(degree)) * 100);
		pos.y -= (speed * Math.cos(Math.toRadians(degree)) * 100);
		dis.x = pos.x / 100;
		dis.y = pos.y / 100;
		setLocation(pos.x / 100, pos.y / 100);
		// if(pos.x<0||pos.y>gScreenWidth*100||pos.y<0||pos.y>gScreenHeight*100)
		// ebullet[i].pic=255;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bulletImg[imgIndex], 0, 0, BWIDTH, BHEIGHT, null);
		// g.drawImage(bulletImg[imgIndex], 0, 0, BWIDTH, BHEIGHT, null);
	}
}
