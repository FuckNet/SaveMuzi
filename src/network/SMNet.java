package network;
// MainView.java : Java Chatting Client 의 핵심부분

// read keyboard --> write to network (Thread 로 처리)
// read network --> write to textArea

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import game.GamePanel;
import home.HomePanel;
import home.LoginPanel;
import lobby.LobbyPanel;
import room.RoomPanel;
import superPanel.ReceiveJPanel;

public class SMNet {
	private static enum NETSTATE {
		Home, Lobby, Room, Game
	};

	// TCP
	private static final int PORT = 30000;
	private static final String IP = "113.198.80.216";

	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	// UDP
	private static final int UDP_PORT = 5000;
	private static final String UDP_IP = "235.0.0.1";

	private MulticastSocket udpSocket;
	private InetAddress udpIP;
	private int udpPort = 0;

	// GUI
	private ReceiveJPanel panel; // 메세지를 받을 패널
	private HomePanel homePanel;
	private GamePanel gamePanel;
	private LobbyPanel lobbyPanel;
	private RoomPanel roomPanel;

	// GAME INFO
	private int playerNo;
	private NETSTATE netState = NETSTATE.Home;

	public SMNet() { // 생성자
	}

	public void setStateToHome() {
		netState = NETSTATE.Home;
	}
	
	public void setStateToLobby() {
		netState = NETSTATE.Lobby;
	}

	public void setStateToRoom() {
		netState = NETSTATE.Room;
	}

	public void setStateToGame() {
	      // UDP
	      try {
	         this.udpSocket = new MulticastSocket(udpPort);
	         this.udpIP = InetAddress.getByName(UDP_IP);
	         udpSocket.joinGroup(udpIP); // 지정한 멀티캐스트 아이피 대역에 합류.
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      netState = NETSTATE.Game;
	}

	public void setHomePanel(HomePanel homePanel) {
		this.homePanel = homePanel;
	}

	public void setLobbyPanel(LobbyPanel lobbyPanel) {
		this.lobbyPanel = lobbyPanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public void setRoomPanel(RoomPanel roomPanel) {
		this.roomPanel = roomPanel;
	}
	
	public void toHomePanel() {
		this.panel = this.homePanel;
	}
	
	public void toLobbyPanel() {
		this.panel = this.lobbyPanel;
	}

	public void toGamePanel() {
		this.panel = this.gamePanel;
	}

	public void toRoomPanel() {
		this.panel = this.roomPanel;
	}

	public void setPort(int portIdx) {
		this.udpPort = PORT + portIdx;
	}

	public void network() {
		// TCP IP 서버에 접속
		try {
			socket = new Socket(IP, PORT);
			if (socket != null) {// socket이 null값이 아닐때 즉! 연결되었을때
				Connection(); // 연결 메소드를 호출
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			System.out.println("소켓 접속 에러!!");
			System.exit(0);
		}
	}

	public void Connection() { // 실직 적인 메소드 연결부분
		try { // 스트림 설정
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);

			// playerNo = dis.readInt();
			// System.out.println(playerNo);
		} catch (IOException e) {
			System.out.println("스트림 설정 에러!!");
		}

		Thread th = new Thread(new Runnable() { // 스레드를 돌려서 서버로부터 메세지를 수신
			@SuppressWarnings("null")
			@Override
			public void run() {
				while (true) {
					switch (netState) {
					case Home:
					case Lobby:
					case Room:
						try {
							byte[] b = new byte[128];
							dis.read(b);
							String msg = new String(b);
							msg = msg.trim();
							System.out.println("받은 메세지 : " + msg);
							panel.receiveMSG(msg);
							// smQueue.addMSG(msg);
							// 받은 메세지 처리
						} catch (IOException e) {
							System.out.println("메세지 수신 에러!!");
							// 서버와 소켓 통신에 문제가 생겼을 경우 소켓을 닫는다
							try {
								os.close();
								is.close();
								dos.close();
								dis.close();
								socket.close();
								break; // 에러 발생하면 while문 종료
							} catch (IOException e1) {

							}
							break;
						}
						break;
					case Game:
						byte[] buffer = new byte[100];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length, udpIP, udpPort);
						try {
							udpSocket.receive(packet);
							String msg = new String(buffer);
							msg = msg.trim();
							panel.receiveMSG(msg);
							System.out.println("받은 메세지 : " + msg);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				} // while문 끝

			}// run메소드 끝
		});
		th.start();
	}

	public void sendToServer(String str) {
		String str2 = playerNo + " " + str + "/";
		sendMSG(str2);
	}

	public void sendMSG(String str) { // 서버로 메세지를 보내는 메소드
		switch (netState) {
		case Home:
		case Lobby:
		case Room:
			try {
				byte[] bb;
				bb = str.getBytes();
				dos.write(bb);
				// .writeUTF(str); dos.flush();
			} catch (IOException e) {
				System.out.println("메세지 송신 에러!!");
				return;
			}
			break;
		case Game:
			byte[] buffer = str.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, udpIP, udpPort);
			try {
				udpSocket.send(packet);
				System.out.println("보낸 메세지 : " + str);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	public int getPlayerNum() {
		return playerNo;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNo = playerNum;
	}
}