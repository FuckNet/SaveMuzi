package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JLabel;

import main.SMFrame;

public class Enemy extends Object {
	// ���ӿ� �����ϴ� �� ĳ���� ���� Ŭ����
	private static int maxPlayerNum;
	private static Player players[];
	private static int level;
	private GamePanel gamePanel;
	protected Point pos;
	protected Point _pos;
	protected Point dis;
	protected Point center;
	protected int imgIndex;
	protected int kind;
	protected int life;
	protected int mode;
	protected int cnt;
	protected int shoottype;
	protected int hitrange;// �پ��� ũ���� �� ĳ���� ������ ���� ���� ���� ������ �߰��Ѵ�.
	protected Bullet bul;
	int eWidth;
	int eHeight;
	
	static Image enemyImg[] = new Image[3];

	public static void enemyInit() {
		for (int i = 0; i < enemyImg.length; i++) {
			enemyImg[i] = Toolkit.getDefaultToolkit().getImage("res/game/enemy" + i + ".png");
		}
	}

	public Enemy(GamePanel main, int img, int x, int y, int kind, int mode, int level) {
		this.gamePanel = main;
		Enemy.level = level;
		pos = new Point(x, y);
		_pos = new Point(x, y);
		dis = new Point(x / 100, y / 100);
		this.kind = kind;
		this.imgIndex = img;
		this.mode = mode;
		switch (imgIndex) {
		case 0:
			eWidth = 36;
			eHeight = 36;
			hitrange = 500;
			//eWidth = enemyImg[img].getWidth(this) / 7;
			//eHeight = enemyImg[img].getHeight(this);
			break;
		case 1:
			eWidth = 186;
			eHeight = 186;
			hitrange = 10000;
			//eWidth = enemyImg[img].getWidth(this);
			//eHeight = enemyImg[img].getHeight(this);
			break;
		case 2:
			eWidth = 36;
			eHeight = 50;
			hitrange = 700;
			//eWidth = enemyImg[img].getWidth(this) / 3;
			//eHeight = enemyImg[img].getHeight(this);
			break;
		}
		//System.out.println("���� �̰ž� : " + imgIndex + ", w : " + eWidth + ", h : " + eHeight + " �̰ŷ� ��");
		center = new Point((x + eWidth) / 100 / 2, (y + eHeight) / 100 / 2);
		life = 3 + GamePanel.RAND(0, 5) * level;// ���� ������ ���� �������� ź�� ��� �ð��� ª������
		cnt = GamePanel.RAND(level * 5, 80);
		shoottype = GamePanel.RAND(0, 4);
		hitrange = 1500;
		switch (kind) {
		case 0:// ť�� �׿���� ���� ����
			break;
		case 1:// ���� ���� ����
			life = 100 + 100 * level;
			mode = 0;
			hitrange = 12000;
			break;
		case 2:// ��ġ �׿���� ���� ����
			life = 10 + GamePanel.RAND(0, 5) * level;
			hitrange = 2000;
			cnt = -(GamePanel.RAND(30, 50));
			break;
		}
		/*
		 * if(kind==1){//������ ��� ���� �Ķ���͸� �������Ѵ� life=400+300*main.level; mode=0;
		 * hitrange=12000; }
		 */
	}

	public static void setMaxPlayerNum(int num) {
		maxPlayerNum = num;
	}

	public static void setPlayers(Player p[]) {
		players = p;
	}

	public boolean move() {
		boolean ret = true;
		int targetPlayer = GamePanel.RAND(1, maxPlayerNum);
		// �켱�� ����
		switch (kind) {
		case 2:// ��ġ �׿������ ���
			if (mode != 4)
				break;
			if (cnt < 30 && cnt % 5 == 0) {
				bul = new Bullet(pos.x, pos.y + eHeight * 100 / 2, 2, 1, 90, 5, 1);
				gamePanel.addBullet(bul);
			}
			if (cnt > 50) {
				if (GamePanel.RAND(1, 100) < 50) {
					mode = 1;// ���� �̵��� ������ ����
					cnt = 30;
				} else {
					mode = 5;// ����
					cnt = 0;
				}
			}
			break;
		case 0:// �Ϲ� �� ĳ������ ���
			switch (shoottype) {// ���� ���¿� ���� ���� �ٸ� ������ �Ѵ�.
			case 0:// �÷��̾ ���� 3���� �����Ѵ�
				if (cnt % 100 == 0 || cnt % 103 == 0 || cnt % 106 == 0) {
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, getAngle(pos.x, pos.y + eHeight * 100 / 2, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100), 3, 1);
					gamePanel.addBullet(bul);
				}
				break;
			case 1:// Ÿ�̸ӿ� ���� 4����ź�� �߻��Ѵ�
				if (cnt % 90 == 0 || cnt % 100 == 0 || cnt % 110 == 0) {
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, (0 + (cnt % 36) * 10) % 360, 3, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, (30 + (cnt % 36) * 10) % 360, 3, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, (60 + (cnt % 36) * 10) % 360, 3, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, (90 + (cnt % 36) * 10) % 360, 3, 1);
					gamePanel.addBullet(bul);
				}
				break;
			case 2:// ª�� �������� �÷��̾� ��ó�� ���� �� �߾� �߻��Ѵ�
				if (cnt % 30 == 0 || cnt % 60 == 0 || cnt % 90 == 0 || cnt % 120 == 0 || cnt % 150 == 0
						|| cnt % 180 == 0) {
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, (getAngle(pos.x, pos.y + eHeight * 100 / 2, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100) + GamePanel.RAND(-20, 20)) % 360, 2, 1);
					gamePanel.addBullet(bul);
				}
				break;
			case 3:// �÷��̾ ���� 3����ź�� �߻��Ѵ�
				if (cnt % 90 == 0 || cnt % 110 == 0 || cnt % 130 == 0) {
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, getAngle(pos.x, pos.y + eHeight * 100 / 2, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100), 2, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, (getAngle(pos.x, pos.y + eHeight * 100 / 2, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100) - 20) % 360, 2, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x + eWidth * 100 / 2, pos.y + eHeight * 100 / 2, 2, 1, (getAngle(pos.x, pos.y + eHeight * 100 / 2, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100) + 20) % 360, 2, 1);
					gamePanel.addBullet(bul);
				}
				break;
			case 4:// �ƹ��� ���ݵ� �����ʴ´�
				break;
			}
			break;
		case 1:// ���� ĳ������ ����, mode�� ���� ���� ����� �ٲ۴�.
			int lv, i;
			switch (mode) {// mode ���� ���� �������� �����Ѵ�. �����ӿ� ���� ���� ��ĵ� �ٲ��ش�.
			case 5:
				if (level >= 10)
					lv = 5;
				else
					lv = (10 - level) * 5;
				if (cnt % lv == 0 || cnt % (lv + 5) == 0 || cnt % (lv + 15) == 0) {
					for (i = 0; i < 4 + (50 - lv) / 5; i++) {
						bul = new Bullet(pos.x, pos.y + eHeight * 100 / 2, 2, 1, (30 * i + (cnt % 36) * 10) % 360, 5, 1);
						gamePanel.addBullet(bul);
					}
					/*
					 * bul=new Bullet(pos.x, pos.y, 2, 1, (30+(cnt%36)*10)%360,
					 * 4); main.bullets.add(bul); bul=new Bullet(pos.x, pos.y,
					 * 2, 1, (60+(cnt%36)*10)%360, 4); main.bullets.add(bul);
					 * bul=new Bullet(pos.x, pos.y, 2, 1, (90+(cnt%36)*10)%360,
					 * 4); main.bullets.add(bul);
					 */
				}
				break;
			case 7:
				if (level >= 10)
					lv = 1;
				else
					lv = 10 - level;
				if (cnt % lv == 0) {
					bul = new Bullet(pos.x - 3000 + GamePanel.RAND(-10, +10) * 100,
							pos.y + eHeight * 100 / 2 + GamePanel.RAND(10, 80) * 100, 2, 1, 90, 5 + (10 - lv) / 2, 1);
					gamePanel.addBullet(bul);
				}
				break;
			}
			break;
		}

		// �̵� ó��
		switch (kind) {
		case 2:// ��ġ �׿������ ���
			/*
			 * ��ġ �׿���� �̵� �ó����� 1. �����ʿ��� �����Ͽ� �������� �̵��ϴ� ȭ�� 70~80% ���� ��ġ�� �����Ѵ�. 2.
			 * �÷��̾��� y��ǥ�� �����Ͽ� ���Ϸ� ���� ���� �̵��ϴ� 3. �����Ͽ� 4. ���� ������ ���а� ���� 5. 2~4�� ��
			 * �� ������ �� 6. �ڷ� ���� ��������
			 */
			switch (mode) {
			case 0:// ���ʿ��� ���������� ����
				pos.x -= 500;
				if (cnt >= 0 && pos.x < SMFrame.SCREEN_WIDTH * 80) {
					mode = 1;
					cnt = 0;
				}
				break;
			case 1:// ���ڸ����� ��� ����
				if (pos.x > SMFrame.SCREEN_WIDTH * 80) {// ���� ���� ����� x��ġ�� �ƴϸ� ��
														// ��
														// �������� �̵��Ѵ�
					mode = 0;
					break;
				}
				if (cnt >= 30) {
					if (pos.y > players[targetPlayer].getY() * 100)
						mode = 3;
					else
						mode = 2;
					cnt = 0;
				}
				break;
			case 2:// �Ʒ��� �̵�
				if (pos.y < SMFrame.SCREEN_HEIGHT * 90 && cnt < 20)
					pos.y += 250;
				else {
					mode = 4;// ����
					cnt = 0;
				}
				break;
			case 3:// ���� �̵�
				if (pos.y > 6400 && cnt < 20)
					pos.y -= 250;
				else {
					mode = 4;// ����
					cnt = 0;
				}
				break;
			case 5:// �ڷ� ���� ����
				pos.x += 350;
				break;
			case 4:// �����ؼ� ���� ������ ���а� �Ѿ� �߻�
				break;
			}
			break;
		case 0:// �Ϲ� ĳ����
			switch (mode) {
			case 0:
				pos.x -= 500;
				pos.y += 80;
				if (pos.x < players[targetPlayer].getX() * 100)
					mode = 2;
				break;
			case 1:
				pos.x -= 500;
				pos.y -= 80;
				if (pos.x < players[targetPlayer].getX() * 100)
					mode = 3;
				break;
			case 2:
				pos.x += 600;
				pos.y += 240;
				break;
			case 3:
				pos.x += 600;
				pos.y -= 240;
				break;
			}
			break;
		case 1:// ����ĳ����
			/*
			 * ���� ĳ���� ������ �ó����� (mode ���� ����) 0. ȭ�� �������� ���� �������� ���´�. 1. �÷��̾��
			 * ���� ������ �Ʒ���(mode=2), �Ʒ��� ������ ����(mode=3) ���� ���� (���� ��ǥ �������� 120��Ʈ)
			 * �̵��Ѵ�. 4. ȭ�� �߾ӱ��� ���´� 5. 0.�� ��ġ���� �ǵ��ư���. 6. ȭ�� �ٱ����� ���� �������. 7. ���
			 * �� �ڸ����� ����� �� mode=1�� ���ư��� 8. ��� �� �ڸ����� ����� �� mode=5�� ��ȯ�Ѵ�
			 */
			if (SMThread.gameCnt == 1200)
				mode = 4;
			if (SMThread.gameCnt == 2210)
				mode = 6;
			switch (mode) {
			case 0:
				pos.x -= 100;
				if (pos.x < 53000)
					mode = 1;
				break;
			case 1:
				if (cnt % 30 == 0) {
					if (pos.y > players[targetPlayer].getY() * 100)
						mode = 3;
					else if (pos.y < players[targetPlayer].getY() * 100)
						mode = 2;
					_pos.x = pos.x;
					_pos.y = pos.y;// �̵� �Ÿ��� üũ�ϱ� ���� �̵��� ���۵Ǵ� ��ġ�� ����
				}
				break;
			case 2:
				if (pos.y + 400 < 42000)
					pos.y += 400;
				else {
					cnt = 0;
					mode = 7;
				}
				if (pos.y - _pos.y > 12000) {
					cnt = 0;
					mode = 7;
				}
				break;
			case 3:
				if (pos.y - 400 > 6000)
					pos.y -= 400;
				else {
					cnt = 0;
					mode = 7;
				}
				if (_pos.y - pos.y > 12000) {
					cnt = 0;
					mode = 7;
				}
				break;
			case 4:
				pos.x -= 800;
				if (pos.x < 30000) {
					mode = 8;
					cnt = 0;
				}
				break;
			case 5:
				pos.x += 350;
				if (pos.x > 53000)
					mode = 1;
				break;
			case 6:
				pos.x += 500;
				break;
			case 7:
				if (cnt > 100)
					mode = 1;
				break;
			case 8:
				if (cnt > 90)
					mode = 5;
				break;
			}
			break;
		}
		dis.x = pos.x / 100;
		dis.y = pos.y / 100;
		center.x = dis.x + eWidth/2;
		center.y = dis.y + eHeight/2;
		if (dis.x < 0 || dis.x > SMFrame.SCREEN_WIDTH || dis.y < 0 || dis.y > SMFrame.SCREEN_HEIGHT)
			ret = false;
		cnt++;
		return ret;
	}

	private int getAngle(int sx, int sy, int dx, int dy) {
		int vx = dx - sx;
		int vy = dy - sy;
		double rad = Math.atan2(vx, vy);
		int degree = (int) ((rad * 180) / Math.PI);
		return (degree + 180);
	}

	public void paint(Graphics g) {
		int sx, sy;
		//System.out.println("�ʺ� : " + eWidth + ", ���� : " + eHeight + ", ���� " + enemyImg[imgIndex]);
		switch (imgIndex) {
		case 0:
			sx = ((cnt / 8) % 7) * eWidth;
			sy = 0;
			g.drawImage(enemyImg[imgIndex], dis.x, dis.y, dis.x + eWidth, dis.y + eHeight, sx, sy, sx + eWidth, sy + eHeight, gamePanel);
			break;
		case 1:
			g.drawImage(enemyImg[imgIndex], dis.x, dis.y, eWidth, eHeight, gamePanel);
			break;
		case 2:// ��ġ �׿����
			switch (mode) {
			case 0:// ���ʿ��� ���������� ����
			case 1:// ���ڸ����� ��� ����
			case 2:// �Ʒ��� �̵�
			case 3:// ���� �̵�
				sx = 0;
				sy = 0;
				g.drawImage(enemyImg[imgIndex], dis.x, dis.y, dis.x + eWidth, dis.y + eHeight, sx, sy, sx + eWidth, sy + eHeight, gamePanel);				
				break;
			case 5:// �ڷ� ���� ����
				sx = 72;
				sy = 0;
				g.drawImage(enemyImg[imgIndex], dis.x, dis.y, dis.x + eWidth, dis.y + eHeight, sx, sy, sx + eWidth, sy + eHeight, gamePanel);
				break;
			case 4:// �����ؼ� ���� ������ ���а� �������� �Ѿ� �߻�
				sx = 36;
				sy = 0;
				g.drawImage(enemyImg[imgIndex], dis.x, dis.y, dis.x + eWidth, dis.y + eHeight, sx, sy, sx + eWidth, sy + eHeight, gamePanel);
				break;
			}
		default:
			break;
		}
	}
}