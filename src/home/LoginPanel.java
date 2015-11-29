package home;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class LoginPanel extends JPanel{
	private static final String BG_LOGIN = "res/background/backgroundLogin.png";
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
	private Image backgroundLogin;
	public LoginPanel() {
		setLayout(null);
		setBounds(0, 0, WIDTH, HEIGHT);
		backgroundLogin = Toolkit.getDefaultToolkit().getImage(BG_LOGIN);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundLogin, 0, 0, getWidth(), getHeight(), this);
	}
}
