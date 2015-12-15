package lobby;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import gui.ChatFieldPanel;
import gui.MyTextArea;
import home.LoginPanel;
import main.SMFrame;
import network.SMNet;
import superPanel.ReceiveJPanel;

public class LobbyPanel extends ReceiveJPanel {// implements Runnable{
	private static final String BG_LOBBY = "res/background/backgroundLobby.png";
	private static final String BG_CHATFIELD = "res/background/backgroundChatField.png";
	private static final String BG_CHAT = "res/background/backgroundChat.png";
	private static final String BTN_CREATEROOM = "res/logo/createRoom.png";
	private static final String BTN_LOGOUT = "res/logo/logoutBtn.png";
	
	private SMFrame smFrame;
	private SMNet smNet;
	private Image backgroundLobby;

	private Vector<RoomInfo> rooms = new Vector<RoomInfo>();
	private JLabel createRoomBtn;
	private JLabel logoutBtn;
	private JScrollPane scrollPane;
	private MyTextArea chatTextArea;
	private ChatFieldPanel chatField;
	private JTextField chatTextField;

	public LobbyPanel(SMFrame smFrame) {
		this.smFrame = smFrame;
		smNet = smFrame.getSMNet();
		setLayout(null);
		backgroundLobby = Toolkit.getDefaultToolkit().getImage(BG_LOBBY);

		chatTextArea = new MyTextArea(BG_CHAT);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(smFrame.getWidth() * 4 / 7, smFrame.getHeight() * 1 / 5, smFrame.getWidth() * 3 / 8,
				smFrame.getHeight() * 4 / 7);
		scrollPane.setViewportView(chatTextArea);
		add(scrollPane);
		
		chatTextField = new JTextField();
		chatTextField.setOpaque(false);
		chatTextField.setBackground(new Color(190, 160, 255));
		chatTextField.setBorder(null);
		chatTextField.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		chatTextField.addActionListener(new MyAction());
		chatTextField.setBounds(10, 10, smFrame.getWidth() * 3 / 8 - 20,
				smFrame.getHeight() * 1 / 10 - 20);
		chatField = new ChatFieldPanel(scrollPane, BG_CHATFIELD);
		chatField.add(chatTextField);
		add(chatField);

		createRoomBtn = new JLabel();
		createRoomBtn.setIcon(new ImageIcon(BTN_CREATEROOM));
		createRoomBtn.setBounds(smFrame.getWidth() * 3 / 5+10, 30, smFrame.getWidth() * 1 / 5,
				smFrame.getHeight() * 1 / 6);
		createRoomBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				smNet.sendMSG("/CREATEROOM ");
				smFrame.roomPanel.addUser(LoginPanel.userID);
				smFrame.sequenceControl("roomPanel", rooms.size() + 1);
			}
		});
		add(createRoomBtn);
		logoutBtn = new JLabel();
		logoutBtn.setIcon(new ImageIcon(BTN_LOGOUT));
		logoutBtn.setBounds(smFrame.getWidth() * 4 / 5, 25, smFrame.getWidth() * 1 / 5,
				smFrame.getHeight() * 1 / 6);
		logoutBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				smFrame.sequenceControl("homePanel", 1);
				smNet.sendMSG("/LOGOUT");
			}
		});
		add(logoutBtn);
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundLobby, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void receiveMSG(String msg) {
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
			removeRoom(Integer.parseInt(splitMsg[1])-1);
		}
		else if (splitMsg[0].equals("/GAMESTARTROOM")) {
			rooms.get(Integer.parseInt(splitMsg[1])-1).gameStart();
		}
		else if (splitMsg[0].equals("/ROOMOUT")) {
			
		}
		else {
			chatTextArea.append(msg + "\n");
			chatTextArea.setCaretPosition(chatTextArea.getText().length());
			chatTextField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
		}
	}

	public void addRoom() {
		RoomInfo room = new RoomInfo();
		room.setLocation(50, RoomInfo.HEIGHT * rooms.size() + 50);
		room.setRoomName("Room " + (rooms.size() + 1));
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

	public void resetRooms() {
		for(int i=0; i<rooms.size(); i++) {
			remove(rooms.get(i));
			rooms.remove(i);
		}
	}
}