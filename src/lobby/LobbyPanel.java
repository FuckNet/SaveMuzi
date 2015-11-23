package lobby;
import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import listener.SMKeyListener;
import main.SMFrame;

public class LobbyPanel extends JPanel {//implements Runnable{
	
	private SMFrame smFrame;
	//private Thread th;
	
	public LobbyPanel(SMFrame smFrame) {
		this.smFrame = smFrame;
		setBackground(Color.CYAN);
		//addKeyListener(new SMKeyListener());
		//th = new Thread(this);
		//th.start();
	}
	
//	public void run() {
//		while(true) {
//			requestFocus();
//			if(SMKeyListener.getKeyState(KeyEvent.VK_ENTER)) {
//				System.out.println("´­¸²");
//				smFrame.changePanel("homePanel");
//			}
//		}
//	}

}
