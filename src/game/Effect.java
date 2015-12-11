package game;

import java.awt.Point;

import javax.swing.JLabel;

public class Effect extends JLabel{
	//폭발 등의 이펙트 관리 클래스
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
			setLocation(dis);
			this.kind=kind;
			this.img=img;
			cnt=16;
		}
}
