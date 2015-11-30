package network;
// MainView.java : Java Chatting Client �� �ٽɺκ�
// read keyboard --> write to network (Thread �� ó��)
// read network --> write to textArea

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import game.GamePanel;

public class SMNet {
	private static final int PORT = 30000;
	private static final String IP = "113.198.80.216";
	
	private Socket socket; // �������
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private GamePanel gamePanel;
	
	private int playerNo;

	public SMNet() { // ������
		network();
	}
	
	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public void network() {
		// ������ ����
		try {
			socket = new Socket(IP, PORT);
			if (socket != null) {// socket�� null���� �ƴҶ� ��! ����Ǿ�����
				Connection(); // ���� �޼ҵ带 ȣ��
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			System.out.println("���� ���� ����!!");
		}

	}

	public void Connection() { // ���� ���� �޼ҵ� ����κ�
		try { // ��Ʈ�� ����
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			
			playerNo = dis.readInt();
			System.out.println(playerNo);
		} catch (IOException e) {
			System.out.println("��Ʈ�� ���� ����!!");
		}
		
		Thread th = new Thread(new Runnable() { // �����带 ������ �����κ��� �޼����� ����
					@SuppressWarnings("null")
					@Override
					public void run() {
						while (true) {
							try {
								byte[] b = new byte[128];
								dis.read(b);
								String msg = new String(b);
								msg = msg.trim();
								gamePanel.receiveGameMSG(msg);
								// ���� �޼��� ó��
							} catch (IOException e) {
								System.out.println("�޼��� ���� ����!!");
								// ������ ���� ��ſ� ������ ������ ��� ������ �ݴ´�
								try {
									os.close();
									is.close();
									dos.close();
									dis.close();
									socket.close();
									break; // ���� �߻��ϸ� while�� ����
								} catch (IOException e1) {

								}

							}
						} // while�� ��

					}// run�޼ҵ� ��
				});
		th.start();
	}

	public void sendToServer(String str) { // ������ �޼����� ������ �޼ҵ�
		try {
			byte[] bb;
			str = playerNo + " " + str;
			bb = str.getBytes();
			dos.write(bb); //.writeUTF(str);
		} catch (IOException e) {
			System.out.println("�޼��� �۽� ����!!");
		}
	}
	
}