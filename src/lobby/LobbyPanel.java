package lobby;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import game.GamePanel;
import listener.SMKeyListener;
import main.SMFrame;
import network.SMNet;
import superPanel.ReceiveJPanel;

public class LobbyPanel extends ReceiveJPanel {//implements Runnable{
   private static final String BG_LOBBY = "res/background/backgroundLobby.png";
   
   private SMFrame smFrame;
   private SMNet smNet;
   private Image backgroundLobby;
   
   private JButton createRoomBtn;
   private Vector<RoomInfo> rooms = new Vector<RoomInfo>();
   
   public LobbyPanel(SMFrame smFrame) {
      this.smFrame = smFrame;
      smNet = smFrame.getSMNet();
      setLayout(null);
      backgroundLobby = Toolkit.getDefaultToolkit().getImage(BG_LOBBY);
      
      createRoomBtn = new JButton("规 积己");
      createRoomBtn.setBounds(smFrame.getWidth()*3/4, smFrame.getY()/3, smFrame.getWidth()*1/5, smFrame.getHeight()*1/6);
      createRoomBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            smNet.sendMSG("/CREATEROOM");
            smFrame.sequenceControl("roomPanel", rooms.size()+1);            
         }
      });
      add(createRoomBtn);
   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(backgroundLobby, 0, 0, getWidth(), getHeight(), this);
   }

   @Override
   public void receiveMSG(String msg) {
      System.out.println("肺厚 皋技瘤");
      String splitMsg[];
      splitMsg = msg.split(" ");
      if(splitMsg.length < 1)
         return;

      // /CREATEROOM : 规 积己
      if(splitMsg[0].equals("/CREATEROOM")) {
         addRoom();
      }
   }

   private void addRoom() {
      RoomInfo room = new RoomInfo();
      room.setLocation(0, RoomInfo.HEIGHT*rooms.size());
      room.setRoomName("规 " + (rooms.size()+1));
      room.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            smNet.sendMSG("/ENTERROOM "+(rooms.indexOf(room)+1));
            smFrame.sequenceControl("roomPanel", (rooms.indexOf(room)+1));
         }
      });
      add(room);
      rooms.add(room);
      System.out.println("规 积己");
      repaint();
   }
}