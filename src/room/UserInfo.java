package room;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserInfo extends JPanel {
	   final static int WIDTH = 400;
	   final static int HEIGHT = 100;
	   private JLabel nameLabel = new JLabel();
	   UserInfo() {
	      setSize(WIDTH, HEIGHT);
	      nameLabel.setBounds(0, 0, WIDTH/3, HEIGHT/2);
	      add(nameLabel);
	   }
	   void setUserName(String name) {
	      nameLabel.setText(name);
	   }
	}