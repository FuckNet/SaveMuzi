package home;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import main.SMFrame;
import network.SMNet;
import superPanel.ReceiveJPanel;

public class HomePanel extends ReceiveJPanel {
	private static final String BG_HOME = "res/background/backgroundHome.png";
	private static final String LOGO = "res/logo/logo.png";
	private static final String PUSHBAR = "res/foreground/pushSpace.png";

	private Thread th;
	
	private SMNet smNet;
	private SMFrame smFrame;
	private Image backgroundHome;
	private Image pushBarImage;

	LoginPanel loginPanel;
	SignUpPanel signUpPanel;

	private JLabel pushBarLabel;
	private JLabel logoLabel;

	public HomePanel(SMFrame smFrame) {
		setLayout(null);
		this.smFrame = smFrame;
		smNet = smFrame.getSMNet();
		
		loginPanel = new LoginPanel(smNet, this);
		loginPanel.setLocation((smFrame.getWidth() - loginPanel.getWidth()) / 2,
				(smFrame.getHeight() - loginPanel.getHeight()) * 3 / 5);
		signUpPanel = new SignUpPanel(smNet, this);
		signUpPanel.setLocation((smFrame.getWidth() - loginPanel.getWidth()) / 2,
				(smFrame.getHeight() - loginPanel.getHeight()) * 3 / 5);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backgroundHome = Toolkit.getDefaultToolkit().getImage(BG_HOME);
		ImageIcon pushBarIcon = new ImageIcon(PUSHBAR);
		pushBarLabel = new JLabel();
		pushBarLabel.setSize(500, 100);
		pushBarLabel.setLocation((smFrame.getWidth() - pushBarLabel.getWidth()) / 2,
				(smFrame.getHeight() - pushBarLabel.getHeight()) * 4 / 5);
		pushBarImage = pushBarIcon.getImage();
		Image pushBarImage2 = pushBarImage.getScaledInstance(pushBarLabel.getWidth(), pushBarLabel.getHeight(),
				java.awt.Image.SCALE_SMOOTH);
		pushBarIcon = new ImageIcon(pushBarImage2);
		pushBarLabel.setIcon(pushBarIcon);

		logoLabel = new JLabel();
		logoLabel.setSize(600, 100);
		logoLabel.setLocation((smFrame.getWidth() - logoLabel.getWidth()) / 2,
				(smFrame.getHeight() - logoLabel.getHeight()) * 1 / 10);

		logoLabel.setIcon(new ImageIcon(LOGO));
		add(logoLabel);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				th.interrupt();
				remove(pushBarLabel);
				showLogin();		
			}
		});
	}

	// Space Bar 입력을 받을 때 Login창을 띄운다.
	   public void start() {

		      th = new Thread(new Runnable() {
		         public void run() {
		            while (true) {
		               try {
		                  add(pushBarLabel);
		                  for (int i = 0; i < 15; i++) { // sleep 도중 키 이벤트 발생이 안되는
		                                          // 것을 방지하기 위해 잘게 쪼갬
		                     Thread.sleep(10);
		                  }
		                  repaint();
		                  remove(pushBarLabel);
		                  for (int i = 0; i < 15; i++) {
		                     Thread.sleep(10);
		                  }
		                  repaint();
		               } catch (InterruptedException e) {
		                  // TODO Auto-generated catch block
		                  // e.printStackTrace();
		                  System.out.println("홈 종료");
		                  break;
		               }
		            }
		         }
		      });
		      
		      th.start();
		   }

	void showLogin() {
		add(loginPanel);
		loginPanel.tf_ID.setText("");
		loginPanel.tf_ID.requestFocus();
		repaint();
	}

	void showSignup() {
		add(signUpPanel);
		signUpPanel.tf_ID.setText("");
		signUpPanel.tf_ID.requestFocus();
		repaint();
	}

	@Override
	public void receiveMSG(String msg) {
		String splitMsg[];
		splitMsg = msg.split(" ");

		if (splitMsg[0].equals("/SUCCESSLOGIN")) {
			loginPanel.userID = splitMsg[1];
			loginPanel.userPW = splitMsg[2];
			loginPanel.writeUserInfo();
			smFrame.sequenceControl("lobbyPanel", 0);
		} else if (splitMsg[0].equals("/NONEXTID")) {
			JOptionPane.showMessageDialog(this, "존재하지 않는 아이디입니다.", "Message", JOptionPane.ERROR_MESSAGE);
		} else if (splitMsg[0].equals("/WRONGPW")) {
			JOptionPane.showMessageDialog(this, "비밀번호가 틀립니다.", "Message", JOptionPane.ERROR_MESSAGE);
		} else if (splitMsg[0].equals("/SUCCESSSIGNUP")) {
			JOptionPane.showMessageDialog(this, "회원가입 완료!", "Message", JOptionPane.ERROR_MESSAGE);
			signUpPanel.tf_ID.setText("");
			signUpPanel.tf_PW.setText("");
			signUpPanel.tf_PWC.setText("");		
		} else if (splitMsg[0].equals("/EXTID")) {		
			JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.", "Message", JOptionPane.ERROR_MESSAGE);				
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundHome, 0, 0, getWidth(), getHeight(), this);
	}

}