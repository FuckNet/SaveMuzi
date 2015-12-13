package room;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import game.GamePanel;
import home.LoginPanel;
import main.SMFrame;
import network.SMNet;
import superPanel.ReceiveJPanel;

public class RoomPanel extends ReceiveJPanel {
	private static final String BG_ROOM = "res/background/backgroundRoom.png";
	private SMFrame smFrame;
	private SMNet smNet;
	private Image backgroundRoom;
	private int roomNum = 0;

	private JButton createRoomBtn;
	private JTextArea chatTextArea;
	private JTextField chatTextField;

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public RoomPanel(SMFrame smFrame) {
		this.smFrame = smFrame;
		smNet = smFrame.getSMNet();
		setLayout(null);
		backgroundRoom = Toolkit.getDefaultToolkit().getImage(BG_ROOM);
		createRoomBtn = new JButton("게임 시작");
		createRoomBtn.setBounds(smFrame.getWidth() * 3 / 5, 0, smFrame.getWidth() * 1 / 5,
				smFrame.getHeight() * 1 / 6);
		createRoomBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String str;
				int seed = (int) (Math.random() * 10);
				str = "/START " + seed;
				smNet.sendMSG(str);
			}
		});
		add(createRoomBtn);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(smFrame.getWidth() * 4 / 7, smFrame.getHeight() * 1 / 5, smFrame.getWidth() * 3 / 8,
				smFrame.getHeight() * 3 / 6);
		add(scrollPane);
		chatTextArea = new JTextArea();
		scrollPane.setViewportView(chatTextArea);
		chatTextArea.setForeground(new Color(255,0,0));
		chatTextArea.setDisabledTextColor(new Color(0, 0, 0));
		chatTextArea.setLineWrap(true);
		chatTextArea.setBackground(new Color(190, 235, 255));
		
		chatTextField = new JTextField();
		chatTextField.setBounds(smFrame.getWidth() * 4 / 7, smFrame.getHeight() *  8 / 10, smFrame.getWidth() * 3 / 8,
				smFrame.getHeight() * 1 / 10);
		chatTextField.setBackground(new Color(190, 160, 255));
		chatTextField.addActionListener(new MyAction());
		add(chatTextField);
	}

	class MyAction implements ActionListener { // 내부클래스로 액션 이벤트 처리 클래스
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == chatTextField) {
				String msg = chatTextField.getText() + "\n";
				msg = String.format("[%s]  %s\n", LoginPanel.userID, msg);
				smNet.sendMSG(msg);
				chatTextField.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				chatTextField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
			}
		}
	}

	@Override
	public void receiveMSG(String msg) {
		String splitMsg[];
		splitMsg = msg.split(" ");
		if (splitMsg.length < 1)
			return;
		// 채팅 : /MSG 1 할말

		// 게임 시작 : /START 4
		if (splitMsg[0].equals("/START")) {
			smNet.setPort(roomNum);
			smNet.setPlayerNum(Integer.parseInt(splitMsg[1]));
			smFrame.sequenceControl("gamePanel", Integer.parseInt(splitMsg[2]));
			GamePanel.rnd = new Random(Long.parseLong(splitMsg[3]));
		}
		else if (splitMsg[0].equals("/CREATEROOM"))
			return;
		else {
			chatTextArea.append(msg + "\n");
			chatTextArea.setCaretPosition(chatTextArea.getText().length());
			chatTextField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundRoom, 0, 0, getWidth(), getHeight(), this);
	}
}