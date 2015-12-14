package lobby;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import home.LoginPanel;
import main.SMFrame;
import network.SMNet;
import superPanel.ReceiveJPanel;

public class LobbyPanel extends ReceiveJPanel {// implements Runnable{
	private static final String BG_LOBBY = "res/background/backgroundLobby.png";

	private SMFrame smFrame;
	private SMNet smNet;
	private Image backgroundLobby;

	private Vector<RoomInfo> rooms = new Vector<RoomInfo>();
	private JButton createRoomBtn;
	private JButton logoutBtn;
	private JTextArea chatTextArea;
	private JTextField chatTextField;

	public LobbyPanel(SMFrame smFrame) {
		this.smFrame = smFrame;
		smNet = smFrame.getSMNet();
		setLayout(null);
		backgroundLobby = Toolkit.getDefaultToolkit().getImage(BG_LOBBY);

		createRoomBtn = new JButton("방 생성");
		createRoomBtn.setBounds(smFrame.getWidth() * 3 / 5, 0, smFrame.getWidth() * 1 / 5,
				smFrame.getHeight() * 1 / 6);
		createRoomBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				smNet.sendMSG("/CREATEROOM ");
				smFrame.roomPanel.addUser(LoginPanel.userID);
				smFrame.sequenceControl("roomPanel", rooms.size() + 1);
			}
		});
		add(createRoomBtn);
		logoutBtn = new JButton("로그아웃");
		logoutBtn.setBounds(smFrame.getWidth() * 4 / 5, 0, smFrame.getWidth() * 1 / 5,
				smFrame.getHeight() * 1 / 6);
		logoutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				smFrame.sequenceControl("homePanel", 1);
				smNet.sendMSG("/LOGOUT");
			}
		});
		add(logoutBtn);

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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundLobby, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void receiveMSG(String msg) {
		System.out.println(msg);
		String splitMsg[];
		splitMsg = msg.split(" ");
		if (splitMsg.length < 1)
			return;

		// /CREATEROOM : 방 생성
		if (splitMsg[0].equals("/CREATEROOM")) {
			addRoom();
		}
		else if (splitMsg[0].equals("/ENTERROOM")) {
			for(int i=1; i<splitMsg.length; i++) {
				smFrame.roomPanel.addUser(splitMsg[i]);
			}
		}
		else if (splitMsg[0].equals("/RMROOM")) {
			System.out.println("방지움 메세지 받음\n");
			removeRoom(Integer.parseInt(splitMsg[1])-1);
		}
		else {
			chatTextArea.append(msg + "\n");
			chatTextArea.setCaretPosition(chatTextArea.getText().length());
			chatTextField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
		}
	}

	public void addRoom() {
		RoomInfo room = new RoomInfo();
		room.setLocation(0, RoomInfo.HEIGHT * rooms.size());
		room.setRoomName("방 " + (rooms.size() + 1));
		room.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				smNet.sendMSG("/ENTERROOM " + (rooms.indexOf(room) + 1) + " " + LoginPanel.userID);
				smFrame.roomPanel.addUser(LoginPanel.userID);
				smFrame.sequenceControl("roomPanel", (rooms.indexOf(room) + 1));
			}
		});
		add(room);
		rooms.add(room);
		repaint();
	}
	public void removeRoom(int roomIdx) {
		remove(rooms.get(roomIdx));
		rooms.removeElementAt(roomIdx);
		repaint();
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
}