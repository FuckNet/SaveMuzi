package network;
// MainView.java : Java Chatting Client �� �ٽɺκ�

// read keyboard --> write to network (Thread �� ó��)
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
import room.RoomPanel;
import superPanel.ReceiveJPanel;

public class SMNet {
   private static enum NETSTATE {
      Chat, Game
   };

   // TCP
   private static final int PORT = 30000;
   private static final String IP = "113.198.80.203";

   private Socket socket; // �������
   private InputStream is;
   private OutputStream os;
   private DataInputStream dis;
   private DataOutputStream dos;

   // UDP
   private static final int UDP_PORT = 5000;
   private static final String UDP_IP = "235.0.0.1";

   private MulticastSocket udpSocket;
   private InetAddress udpIP;

   // GUI
   private ReceiveJPanel panel; // �޼����� ���� �г�
   private GamePanel gamePanel;
   private RoomPanel roomPanel;

   // GAME INFO
   private int playerNo;
   private NETSTATE netState = NETSTATE.Chat;

   public SMNet() { // ������
   }

   public void setStateToGame() {
      netState = NETSTATE.Game;
   }

   public void setGamePanel(GamePanel gamePanel) {
      this.gamePanel = gamePanel;
   }

   public void setRoomPanel(RoomPanel roomPanel) {
      this.roomPanel = roomPanel;
   }

   public void toGamePanel() {
      this.panel = this.gamePanel;
   }

   public void toRoomPanel() {
      this.panel = this.roomPanel;
   }

   public void network() {
      // TCP IP ������ ����
      try {
         socket = new Socket(IP, PORT);
         if (socket != null) {// socket�� null���� �ƴҶ� ��! ����Ǿ�����
            Connection(); // ���� �޼ҵ带 ȣ��
         }
      } catch (UnknownHostException e) {

      } catch (IOException e) {
         System.out.println("���� ���� ����!!");
      }

      // UDP

      try {
         this.udpSocket = new MulticastSocket(UDP_PORT);
         this.udpIP = InetAddress.getByName(UDP_IP);
         udpSocket.joinGroup(udpIP); // ������ ��Ƽĳ��Ʈ ������ �뿪�� �շ�.
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
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
               switch (netState) {
               case Chat:
                  try {
                     byte[] b = new byte[128];
                     dis.read(b);
                     String msg = new String(b);
                     msg = msg.trim();
                     if (msg.equals("PING/")) {
                        sendMSG(msg);
                        continue;
                     }
                     panel.receiveMSG(msg);
                     // smQueue.addMSG(msg);
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
                     break;
                  }
               case Game:
                  byte[] buffer = new byte[100];
                  DatagramPacket packet = new DatagramPacket(buffer, buffer.length, udpIP, UDP_PORT);
                  try {
                     udpSocket.receive(packet);
                     String msg = new String(buffer);
                     msg = msg.trim();
                     panel.receiveMSG(msg);
                     System.out.println("���� �޼��� : " + msg);
                  } catch (IOException e) {
                     e.printStackTrace();
                  }
                  break;
               }
            } // while�� ��

         }// run�޼ҵ� ��
      });
      th.start();
   }

   public void sendToServer(String str) {
      String str2 = playerNo + " " + str + "/";
      sendMSG(str2);
   }

   public void sendMSG(String str) { // ������ �޼����� ������ �޼ҵ�
      switch (netState) {
      case Chat:
         try {
            byte[] bb;
            bb = str.getBytes();
            dos.write(bb);
            // .writeUTF(str); dos.flush();
         } catch (IOException e) {
            System.out.println("�޼��� �۽� ����!!");
            return;
         }
         break;
      case Game:
         byte[] buffer = str.getBytes();
         DatagramPacket packet = new DatagramPacket(buffer, buffer.length, udpIP, UDP_PORT);
         try {
            udpSocket.send(packet);
            System.out.println("���� �޼��� : " + str);
         } catch (IOException e) {
            e.printStackTrace();
         }
         break;
      }
   }
}