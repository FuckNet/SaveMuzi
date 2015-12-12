package room;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import game.GamePanel;
import main.SMFrame;
import network.SMNet;
import superPanel.ReceiveJPanel;

public class RoomPanel extends ReceiveJPanel {

	private SMFrame smFrame;
	private SMNet smNet;
	
	public RoomPanel(SMFrame smFrame) {
		this.smFrame = smFrame;
		smNet = smFrame.getSMNet();
		setLayout(null);
		
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					String str;
					int seed = (int)(Math.random() * 10);
					str = "/START "+ seed;
					smNet.sendMSG(str);
				}
			}
		});
		
	}
	
	@Override
	public void receiveMSG(String msg) {
		// ä�� : /MSG 1 �Ҹ�
		String splitMsg[];
		splitMsg = msg.split(" ");
		if(splitMsg.length < 1)
			return;

		// ���� ���� : /START 4
		if(splitMsg[0].equals("/START")) {
			smFrame.sequenceControl("gamePanel", Integer.parseInt(splitMsg[1]));
			GamePanel.rnd = new Random(Long.parseLong(splitMsg[1]));
		}
		
	}
	
}
