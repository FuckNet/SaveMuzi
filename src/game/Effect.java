package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

//Effect
public class Effect extends Object {
	// 폭발 등의 이펙트 관리 클래스
	private Point pos;
	private Point _pos;
	private Point dis;
	private int img;
	private int kind;
	protected int cnt;
	
	public static final int EFWIDTH = 64;
	public static final int EFHEIGHT = 64;
	
	static Image effectImg[] = new Image[1];

	public static void effectInit() {
		for (int i = 0; i < effectImg.length; i++) {
			effectImg[i] = Toolkit.getDefaultToolkit().getImage("res/game/effect" + i + ".png");
		}
	}

	public Effect(int img, int x, int y, int kind) {
		pos = new Point(x, y);
		_pos = new Point(x, y);
		dis = new Point(x / 100, y / 100);
		this.kind = kind;
		this.img = img;
		cnt = 16;
	}
	
	public void paint(Graphics g) {
		int sx = ((16-cnt)%4)*64;
		int sy = ((16-cnt)/4)*64;
		g.drawImage(effectImg[kind], dis.x, dis.y, dis.x + EFWIDTH, dis.y + EFHEIGHT, sx, sy, sx+EFWIDTH, sy+EFHEIGHT, null);
	}
}
