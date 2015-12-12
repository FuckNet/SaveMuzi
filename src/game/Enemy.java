package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JLabel;

import main.SMFrame;

public class Enemy extends JLabel {
	// 게임에 등장하는 적 캐릭터 관리 클래스
	private static int maxPlayerNum;
	private static Player players[];
	private static int level;
	private GamePanel gamePanel;
	protected Point pos;
	protected Point _pos;
	protected Point dis;
	protected int imgIndex;
	protected int kind;
	protected int life;
	protected int mode;
	protected int cnt;
	protected int shoottype;
	protected int hitrange;// 다양한 크기의 적 캐릭터 수용을 위해 명중 판정 범위를 추가한다.
	protected Bullet bul;

	private static final int NEMONUNWIDTH = 40, NEMONUNHEIGHT = 40;
	private static final int GANGCIWIDTH = 70, GANGCIHEIGHT = 70;

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
		int width;
		int height;
		switch (imgIndex) {
		case 0:
			width = enemyImg[img].getWidth(this) / 7;
			height = enemyImg[img].getHeight(this);
			break;
		case 1:
			width = enemyImg[img].getWidth(this);
			height = enemyImg[img].getHeight(this);
			break;
		case 2:
			width = enemyImg[img].getWidth(this) / 3;
			height = enemyImg[img].getHeight(this);
			break;
		default:
			width = enemyImg[img].getWidth(this);
			height = enemyImg[img].getHeight(this);
		}
		setSize(width, height);
		life = 3 + GamePanel.RAND(0, 5) * level;// 게임 레벨에 따라 라이프와 탄을 쏘는 시간이 짧아진다
		cnt = GamePanel.RAND(level * 5, 80);
		shoottype = GamePanel.RAND(0, 4);
		hitrange = 1500;
		switch (kind) {
		case 0:// 큐브 네우로이 전용 셋팅
			break;
		case 1:// 보스 전용 셋팅
			life = 100 + 100 * level;
			mode = 0;
			hitrange = 12000;
			break;
		case 2:// 위치 네우로이 전용 셋팅
			life = 10 + GamePanel.RAND(0, 5) * level;
			hitrange = 2000;
			cnt = -(GamePanel.RAND(30, 50));
			break;
		}
		/*
		 * if(kind==1){//보스일 경우 세부 파라미터를 재지정한다 life=400+300*main.level; mode=0;
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
		// 우선은 공격
		switch (kind) {
		case 2:// 위치 네우로이의 경우
			if (mode != 4)
				break;
			if (cnt < 30 && cnt % 5 == 0) {
				bul = new Bullet(pos.x, pos.y, 2, 1, 90, 5, 1);
				gamePanel.addBullet(bul);
			}
			if (cnt > 50) {
				if (GamePanel.RAND(1, 100) < 50) {
					mode = 1;// 새로 이동할 방향을 결정
					cnt = 30;
				} else {
					mode = 5;// 퇴장
					cnt = 0;
				}
			}
			break;
		case 0:// 일반 적 캐릭터일 경우
			switch (shoottype) {// 공격 형태에 따라 각기 다른 공격을 한다.
			case 0:// 플레이어를 향해 3발을 점사한다
				if (cnt % 100 == 0 || cnt % 103 == 0 || cnt % 106 == 0) {
					bul = new Bullet(pos.x, pos.y, 2, 1, getAngle(pos.x, pos.y, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100), 3, 1);
					gamePanel.addBullet(bul);
				}
				break;
			case 1:// 타이머에 맞춰 4방향탄을 발사한다
				if (cnt % 90 == 0 || cnt % 100 == 0 || cnt % 110 == 0) {
					bul = new Bullet(pos.x, pos.y, 2, 1, (0 + (cnt % 36) * 10) % 360, 3, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x, pos.y, 2, 1, (30 + (cnt % 36) * 10) % 360, 3, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x, pos.y, 2, 1, (60 + (cnt % 36) * 10) % 360, 3, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x, pos.y, 2, 1, (90 + (cnt % 36) * 10) % 360, 3, 1);
					gamePanel.addBullet(bul);
				}
				break;
			case 2:// 짧은 간격으로 플레이어 근처를 향해 한 발씩 발사한다
				if (cnt % 30 == 0 || cnt % 60 == 0 || cnt % 90 == 0 || cnt % 120 == 0 || cnt % 150 == 0
						|| cnt % 180 == 0) {
					bul = new Bullet(pos.x, pos.y, 2, 1, (getAngle(pos.x, pos.y, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100) + GamePanel.RAND(-20, 20)) % 360, 2, 1);
					gamePanel.addBullet(bul);
				}
				break;
			case 3:// 플레이어를 향해 3갈래탄을 발사한다
				if (cnt % 90 == 0 || cnt % 110 == 0 || cnt % 130 == 0) {
					bul = new Bullet(pos.x, pos.y, 2, 1, getAngle(pos.x, pos.y, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100), 2, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x, pos.y, 2, 1, (getAngle(pos.x, pos.y, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100) - 20) % 360, 2, 1);
					gamePanel.addBullet(bul);
					bul = new Bullet(pos.x, pos.y, 2, 1, (getAngle(pos.x, pos.y, players[targetPlayer].getX() * 100,
							players[targetPlayer].getY() * 100) + 20) % 360, 2, 1);
					gamePanel.addBullet(bul);
				}
				break;
			case 4:// 아무런 공격도 하지않는다
				break;
			}
			break;
		case 1:// 보스 캐릭터일 경우는, mode에 따라 공격 방식을 바꾼다.
			int lv, i;
			switch (mode) {// mode 값은 원래 움직임을 결정한다. 움직임에 맞춰 공격 방식도 바꿔준다.
			case 5:
				if (level >= 10)
					lv = 5;
				else
					lv = (10 - level) * 5;
				if (cnt % lv == 0 || cnt % (lv + 5) == 0 || cnt % (lv + 15) == 0) {
					for (i = 0; i < 4 + (50 - lv) / 5; i++) {
						bul = new Bullet(pos.x, pos.y, 2, 1, (30 * i + (cnt % 36) * 10) % 360, 5, 1);
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
							pos.y + GamePanel.RAND(10, 80) * 100, 2, 1, 90, 5 + (10 - lv) / 2, 1);
					gamePanel.addBullet(bul);
				}
				break;
			}
			break;
		}

		// 이동 처리
		switch (kind) {
		case 2:// 위치 네우로이의 경우
			/*
			 * 위치 네우로이 이동 시나리오 1. 오른쪽에서 등장하여 왼쪽으로 이동하다 화면 70~80% 정도 위치에 정지한다. 2.
			 * 플레이어의 y좌표를 참조하여 상하로 일정 간격 이동하다 3. 정지하여 4. 손을 앞으로 내밀고 공격 5. 2~4를 몇
			 * 번 수행한 후 6. 뒤로 돌아 물러간다
			 */
			switch (mode) {
			case 0:// 왼쪽에서 오른쪽으로 등장
				pos.x -= 500;
				if (cnt >= 0 && pos.x < SMFrame.SCREEN_WIDTH * 80) {
					mode = 1;
					cnt = 0;
				}
				break;
			case 1:// 제자리에서 잠시 정지
				if (pos.x > SMFrame.SCREEN_WIDTH * 80) {// 만일 아직 충분한 x위치가 아니면 좀
														// 더
														// 왼쪽으로 이동한다
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
			case 2:// 아래로 이동
				if (pos.y < SMFrame.SCREEN_HEIGHT * 90 && cnt < 20)
					pos.y += 250;
				else {
					mode = 4;// 공격
					cnt = 0;
				}
				break;
			case 3:// 위로 이동
				if (pos.y > 6400 && cnt < 20)
					pos.y -= 250;
				else {
					mode = 4;// 공격
					cnt = 0;
				}
				break;
			case 5:// 뒤로 돌아 퇴장
				pos.x += 350;
				break;
			case 4:// 정지해서 손을 앞으로 내밀고 총알 발사
				break;
			}
			break;
		case 0:// 일반 캐릭터
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
		case 1:// 보스캐릭터
			/*
			 * 보스 캐릭터 움직임 시나리오 (mode 값에 따라) 0. 화면 우측에서 조금 안쪽으로 들어온다. 1. 플레이어보다
			 * 위에 있으면 아래로(mode=2), 아래에 있으면 위로(mode=3) 일정 간격 (실제 좌표 기준으로 120도트)
			 * 이동한다. 4. 화면 중앙까지 나온다 5. 0.의 위치까지 되돌아간다. 6. 화면 바깥으로 나가 사라진다. 7. 잠시
			 * 그 자리에서 대기한 뒤 mode=1로 돌아간다 8. 잠시 그 자리에서 대기한 뒤 mode=5로 전환한다
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
					_pos.y = pos.y;// 이동 거리를 체크하기 위해 이동이 시작되는 위치를 지정
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
		if (dis.x < 0 || dis.x > SMFrame.SCREEN_WIDTH || dis.y < 0 || dis.y > SMFrame.SCREEN_HEIGHT)
			ret = false;
		setLocation(pos.x / 100, pos.y / 100);
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

	@Override
	public void paintComponent(Graphics g) {
		int imgw, imgh, sx, sy, wd, ht, anc;
		switch (imgIndex) {
		case 0:
			sx = ((cnt / 8) % 7) * 36;
			sy = 0;
			wd = 36;
			ht = 36;
			anc = 4;
			if (dis.x < 0) {
				wd += dis.x;
				sx -= dis.x;
				dis.x = 0;
			}
			if (dis.y < 0) {
				ht += dis.y;
				sy -= dis.y;
				dis.y = 0;
			}
			if (wd < 0 || ht < 0)
				return;
			dis.x = dis.x - (anc % 3) * (wd / 2);
			dis.y = dis.y - (anc / 3) * (ht / 2);
			g.setClip(dis.x, dis.y, wd, ht);
			g.drawImage(enemyImg[imgIndex], dis.x - sx, dis.y - sy, gamePanel);
			g.setClip(0, 0, SMFrame.SCREEN_WIDTH + 10, SMFrame.SCREEN_HEIGHT + 30);
			// drawImageAnc(enemyImg[0], dis.x, dis.y, ((cnt/8)%7)*36,0, 36,36,
			// 4);
			break;
		case 1:
			anc = 4;
			imgw = enemyImg[imgIndex].getWidth(gamePanel);
			imgh = enemyImg[imgIndex].getHeight(gamePanel);
			dis.x = dis.x - (anc % 3) * (imgw / 2);
			dis.y = dis.y - (anc / 3) * (imgh / 2);

			g.drawImage(enemyImg[imgIndex], 0, 0, gamePanel);
			// gc.drawImage(img, x,y, this);
			// drawImageAnc(enemyImg[1], dis.x, dis.y, 4);//보스 출력
			break;
		case 2:// 위치 네우로이
			switch (mode) {
			case 0:// 왼쪽에서 오른쪽으로 등장
			case 1:// 제자리에서 잠시 정지
			case 2:// 아래로 이동
			case 3:// 위로 이동
				sx = 0;
				sy = 0;
				wd = 36;
				ht = 50;
				anc = 4;
				if (dis.x < 0) {
					wd += dis.x;
					sx -= dis.x;
					dis.x = 0;
				}
				if (dis.y < 0) {
					ht += dis.y;
					sy -= dis.y;
					dis.y = 0;
				}
				if (wd < 0 || ht < 0)
					return;
				dis.x = dis.x - (anc % 3) * (wd / 2);
				dis.y = dis.y - (anc / 3) * (ht / 2);
				g.setClip(dis.x, dis.y, wd, ht);
				g.drawImage(enemyImg[imgIndex], dis.x - sx, dis.y - sy, gamePanel);
				g.setClip(0, 0, SMFrame.SCREEN_WIDTH + 10, SMFrame.SCREEN_HEIGHT + 30);
				// drawImageAnc(enemyImg[2], dis.x, dis.y, 0,0,36,50,4);
				break;
			case 5:// 뒤로 돌아 퇴장
				sx = 72;
				sy = 0;
				wd = 36;
				ht = 50;
				anc = 4;
				if (dis.x < 0) {
					wd += dis.x;
					sx -= dis.x;
					dis.x = 0;
				}
				if (dis.y < 0) {
					ht += dis.y;
					sy -= dis.y;
					dis.y = 0;
				}
				if (wd < 0 || ht < 0)
					return;
				dis.x = dis.x - (anc % 3) * (wd / 2);
				dis.y = dis.y - (anc / 3) * (ht / 2);
				g.setClip(dis.x, dis.y, wd, ht);
				g.drawImage(enemyImg[imgIndex], dis.x - sx, dis.y - sy, gamePanel);
				g.setClip(0, 0, SMFrame.SCREEN_WIDTH + 10, SMFrame.SCREEN_HEIGHT + 30);
				// drawImageAnc(enemyImg[2], dis.x, dis.y, 72,0,36,50,4);
				break;
			case 4:// 정지해서 손을 앞으로 내밀고 수평으로 총알 발사
				sx = 36;
				sy = 0;
				wd = 36;
				ht = 50;
				anc = 4;
				if (dis.x < 0) {
					wd += dis.x;
					sx -= dis.x;
					dis.x = 0;
				}
				if (dis.y < 0) {
					ht += dis.y;
					sy -= dis.y;
					dis.y = 0;
				}
				if (wd < 0 || ht < 0)
					return;
				dis.x = dis.x - (anc % 3) * (wd / 2);
				dis.y = dis.y - (anc / 3) * (ht / 2);
				g.setClip(dis.x, dis.y, wd, ht);
				g.drawImage(enemyImg[imgIndex], dis.x - sx, dis.y - sy, gamePanel);
				g.setClip(0, 0, SMFrame.SCREEN_WIDTH + 10, SMFrame.SCREEN_HEIGHT + 30);
				// drawImageAnc(enemyImg[2], dis.x, dis.y, 36,0,36,50,4);
				break;
			}
		default:
			break;
		}
	}
}