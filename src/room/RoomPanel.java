package room;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import game.GamePanel;
import game.SMThread;
import gui.ChatFieldPanel;
import gui.MyTextArea;
import home.LoginPanel;
import main.SMFrame;
import network.SMNet;
import superPanel.ReceiveJPanel;

public class RoomPanel extends ReceiveJPanel {
	private static final String BG_ROOM = "res/background/backgroundRoom.png";
	private static final String BG_CHATFIELD = "res/background/backgroundChatField.png";
	private static final String BG_CHAT = "res/background/backgroundChat4.png";
	private static final String BTN_GAMESTART = "res/logo/gameStart.png";
	private static final String BTN_ROOMOUT = "res/logo/roomOut.png";
	
	private SMFrame smFrame;
	private SMNet smNet;
	private Image backgroundRoom;
	private int roomNum = 0;

	private JLabel createRoomBtn;
	private JLabel roomOutBtn;
	private JScrollPane scrollPane;
	private MyTextArea chatTextArea;
	private ChatFieldPanel chatField;
	private JTextField chatTextField;
	
	private Vector<UserInfo> users = new Vector<UserInfo>();

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public RoomPanel(SMFrame smFrame) {
		this.smFrame = smFrame;
		smNet = smFrame.getSMNet();
		setLayout(null);
		backgroundRoom = Toolkit.getDefaultToolkit().getImage(BG_ROOM);

		createRoomBtn = new JLabel();
		createRoomBtn.setIcon(new ImageIcon(BTN_GAMESTART));
		createRoomBtn.setBounds(smFrame.getWidth() * 6 / 11, 0, smFrame.getWidth() * 1 / 5,
				smFrame.getHeight() * 1 / 6);
		createRoomBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String str;
				int seed = (int) (Math.random() * 10);
				str = "/START " + seed;
				smNet.sendMSG(str);
			}
		});
		add(createRoomBtn);

		roomOutBtn = new JLabel();
		roomOutBtn.setIcon(new ImageIcon(BTN_ROOMOUT));
		roomOutBtn.setBounds(smFrame.getWidth() * 9 / 12, 0, smFrame.getWidth() * 1 / 5,
				smFrame.getHeight() * 1 / 6);
		roomOutBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				smNet.sendMSG("/ROOMOUT");
			}
		});
		add(roomOutBtn);
		
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
			SMThread.seed = Integer.parseInt(splitMsg[3]);
		}
		else if (splitMsg[0].equals("/CREATEROOM"))
			return;
		else if (splitMsg[0].equals("/ENTERROOM")) {
			for(int i=1; i<splitMsg.length; i++) {
				addUser(splitMsg[i]);
			}
		}
		else if (splitMsg[0].equals("/ENTERUSER")) {
			addUser(splitMsg[1]);
		}
		else if (splitMsg[0].equals("/RMROOM")) {
			int idx = Integer.parseInt(splitMsg[1])-1;
			remove(users.get(idx));
			users.removeElementAt(idx);
			repaint();
		}
		else if (splitMsg[0].equals("/ROOMOUT")) {
			for(int i=0; i<users.size(); i++) {
				remove(users.get(i));
				users.removeElementAt(i);
			}
			smFrame.lobbyPanel.resetRooms();
			smFrame.sequenceControl("lobbyPanel", Integer.parseInt(splitMsg[1]));
		}
		else if (splitMsg[0].equals("/ROOMOUTPLAYER")) {
			int idx = Integer.parseInt(splitMsg[1])-1;
			remove(users.get(idx));
			users.removeElementAt(idx);
			repaint();
		}
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

	public void addUser(String userID) {
		UserInfo user = new UserInfo();
		System.out.println(users.size());
		user.setLocation(0, UserInfo.HEIGHT * users.size());
		user.setUserName(userID);
		users.add(user);
		add(user);
		repaint();
	}
}