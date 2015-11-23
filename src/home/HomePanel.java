package home;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import listener.SMKeyListener;
import main.SMFrame;

public class HomePanel extends JPanel{
	
	private SMFrame smframe;
	private Image backgroundHome;// pushSpace;//bufferImage;
	
	//private Graphics graphis;
	
	private LoginPanel loginPanel;
	private SignUpPanel signUpPanel;
	
	private JLabel pushBarLabel;
	private JLabel logoLabel;
	
	public HomePanel(SMFrame smFrame) {
		setLayout(null);
		this.smframe = smFrame;
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backgroundHome = Toolkit.getDefaultToolkit().getImage("res/background/backgroundHome.png");
		//pushSpace = Toolkit.getDefaultToolkit().getImage("res/foreground/pushSpace.png");
		//bufferImage = createImage(getWidth(), getHeight());
		pushBarLabel = new JLabel();
		pushBarLabel.setSize(400, 100);
		pushBarLabel.setLocation((smFrame.getWidth() - pushBarLabel.getWidth()) / 2,
				(smFrame.getHeight() - pushBarLabel.getHeight())*4 / 5);
		pushBarLabel.setIcon(new ImageIcon("res/foreground/pushSpace.png"));
		
		logoLabel = new JLabel();
		logoLabel.setSize(600, 100);
		logoLabel.setLocation((smFrame.getWidth() - logoLabel.getWidth()) / 2,
				(smFrame.getHeight() - logoLabel.getHeight())*1 / 5);
		
		logoLabel.setIcon(new ImageIcon("res/logo/logo.png"));
		add(logoLabel);
		addKeyListener(new SMKeyListener());
		
		
	}
	// Space Bar 입력을 받을 때 Login창을 띄운다.
	public void start() {

		while(true) {
			try {
				add(pushBarLabel);
				for(int i = 0; i < 15; i++) {	// sleep 도중 키 이벤트 발생이 안되는 것을 방지하기 위해 잘게 쪼갬
					Thread.sleep(10);
					if(SMKeyListener.getKeyState(KeyEvent.VK_SPACE))
						return;
				}
				repaint();
				remove(pushBarLabel);
				for(int i = 0; i < 15; i++) {
					Thread.sleep(10);
					if(SMKeyListener.getKeyState(KeyEvent.VK_SPACE))
						return;
				}
				repaint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void startLogin() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundHome, 0, 0, getWidth(), getHeight(), this);
	}
	
}
