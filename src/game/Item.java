package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

// Item
public class Item extends Object {
	// 아이템 관리 클래스
	protected Point pos;
	protected Point dis;
	protected Point center;
	private int speed;
	private int cnt;
	protected int kind;
	
	private static SMThread smThread;
	
	public static final int IWIDTH = 36;
	public static final int IHEIGHT = 36;


	static Image itemImg[] = new Image[3];

	public static void itemInit(SMThread smThread) {
		for (int i = 0; i < itemImg.length; i++) {
			itemImg[i] = Toolkit.getDefaultToolkit().getImage("res/game/item" + i + ".png");
		}
		Item.smThread = smThread;
	}

	
	public Item(int x, int y, int kind) {
		this.kind = kind;
		pos = new Point(x, y);
		dis = new Point(x / 100, y / 100);
		center = new Point(dis.x + IWIDTH/2, dis.y + IHEIGHT/2);
		speed = -200;
		cnt = 0;
	}

	public boolean move() {
		boolean ret = false;
		pos.x -= speed;
		cnt++;
		if (cnt >= 50)
			speed = 200;
		else if (cnt >= 30)
			speed = 100;
		else if (cnt >= 20)
			speed = -100;
		dis.x = pos.x / 100;
		if (pos.x < 0)
			ret = true;
		dis.x = pos.x / 100;
		center.x = dis.x + IWIDTH/2;
		return ret;
	}
	
	public void paint(Graphics g) {
		int sx = ((smThread.cnt/4)%7)*36;
		g.drawImage(itemImg[kind], dis.x, dis.y, dis.x + IWIDTH, dis.y + IHEIGHT, sx, 0, sx+IWIDTH, IHEIGHT, null);
	}
}
