package lobby;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RoomInfo extends JPanel {
	final static int WIDTH = 400;
	final static int HEIGHT = 100;
	private JLabel nameLabel = new JLabel();
	private JLabel gameStartLabel = new JLabel();
	
	RoomInfo() {
		setSize(WIDTH, HEIGHT);
		nameLabel.setBounds(0, 0, WIDTH / 3, HEIGHT / 2);
		gameStartLabel.setBounds(0, 0, WIDTH, HEIGHT);
		gameStartLabel.setText("∞‘¿” ¡ﬂ");
		
		add(nameLabel);
	}

	void setRoomName(String name) {
		nameLabel.setText(name);
	}

	public void gameStart() {
		remove(nameLabel);
		add(gameStartLabel);
		repaint();
	}
}