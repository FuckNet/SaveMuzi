package lobby;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RoomInfo extends JPanel {
	private static final String BG_ROOMINFO = "res/logo/roomInfo2.png";
	private static final String BG_ROOMSTART = "res/logo/roomInfoStart.png";
	final static int WIDTH = 400;
	final static int HEIGHT = 100;
	
	private JLabel gameStartLabel = new JLabel();
	private Image backgroundRoomInfo;
	private Image backgroundRoomStart;
	private Image curImage;
	private int fontSize = 30;
	private String name;
	
	RoomInfo() {
		setSize(WIDTH, HEIGHT);
		gameStartLabel.setBounds(0, 0, WIDTH, HEIGHT);
		gameStartLabel.setText("∞‘¿” ¡ﬂ");
		backgroundRoomInfo = Toolkit.getDefaultToolkit().getImage(BG_ROOMINFO);
		backgroundRoomStart = Toolkit.getDefaultToolkit().getImage(BG_ROOMSTART);
		curImage = backgroundRoomInfo;
	}

	void setRoomName(String name) {
		this.name = name;
	}

	public void gameStart() {
		name = "∞‘¿” ¡ﬂ";
		curImage = backgroundRoomStart;
		repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(curImage, 0, 0, getWidth(), getHeight(), this);
		g.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.PLAIN, fontSize));
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(name, (WIDTH - name.length()*fontSize) / 2, (HEIGHT + fontSize - 10)/2);
	}
}