package game;

import java.awt.Point;

public class Effect {
	//���� ���� ����Ʈ ���� Ŭ����
	private Point pos;
	private Point _pos;
	private Point dis;
	private int img;
	private int kind;
	protected int cnt;
		public Effect(int img, int x, int y, int kind){
			pos=new Point(x,y);
			_pos=new Point(x,y);
			dis=new Point(x/100,y/100);
			this.kind=kind;
			this.img=img;
			cnt=16;
		}
}
