package room;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserInfo extends JPanel {
	private static final String BG_USERINFO = "res/logo/userInfo.png";
	final static int WIDTH = 400;
	final static int HEIGHT = 100;
	private Image backgroundUserInfo;
	private int fontSize = 30;
	private String name;
	UserInfo() {
		backgroundUserInfo = Toolkit.getDefaultToolkit().getImage(BG_USERINFO);
		setSize(WIDTH, HEIGHT);
	}

	void setUserName(String name) {
		this.name = name;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(backgroundUserInfo, 0, 0, getWidth(), getHeight(), this);
		g.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, fontSize));
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(name, (WIDTH - name.length()*fontSize) / 2, (HEIGHT + fontSize - 10)/2);
	}
}