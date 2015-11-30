package network;
// MainView.java : Java Chatting Client 의 핵심부분
// read keyboard --> write to network (Thread 로 처리)
// read network --> write to textArea

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SMNet {
	private static final int PORT = 30000;
	private static final String IP = "127.0.0.1";
	
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private int playerNo;

	public SMNet() { // 생성자
		network();
	}

	public void network() {
		// 서버에 접속
		try {
			socket = new Socket(IP, PORT);
			if (socket != null) {// socket이 null값이 아닐때 즉! 연결되었을때
				Connection(); // 연결 메소드를 호출
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			System.out.println("소켓 접속 에러!!");
		}

	}

	public void Connection() { // 실직 적인 메소드 연결부분
		try { // 스트림 설정
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			
			playerNo = dis.readInt();
			System.out.println(playerNo);
		} catch (IOException e) {
			System.out.println("스트림 설정 에러!!");
		}
		
		Thread th = new Thread(new Runnable() { // 스레드를 돌려서 서버로부터 메세지를 수신
					@SuppressWarnings("null")
					@Override
					public void run() {
						while (true) {
							try {
								byte[] b = new byte[128];
								dis.read(b);
								String msg = new String(b);
								msg = msg.trim();
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

							}
						} // while문 끝

					}// run메소드 끝
				});
		th.start();
	}

	public void sendToServer(String str) { // 서버로 메세지를 보내는 메소드
		try {
			byte[] bb;
			str = playerNo + " " + str;
			bb = str.getBytes();
			dos.write(bb); //.writeUTF(str);
		} catch (IOException e) {
			System.out.println("메세지 송신 에러!!");
		}
	}
	
}