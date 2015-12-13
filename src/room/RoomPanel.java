package room;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import game.GamePanel;
import main.SMFrame;
import network.SMNet;
import superPanel.ReceiveJPanel;

public class RoomPanel extends ReceiveJPanel {
   private static final String BG_ROOM = "res/background/backgroundRoom.png";
   private SMFrame smFrame;
   private SMNet smNet;
   private Image backgroundRoom;
   private int roomNum = 0;
   
   public void setRoomNum(int roomNum) {
      this.roomNum = roomNum;
   }
   public RoomPanel(SMFrame smFrame) {
      this.smFrame = smFrame;
      smNet = smFrame.getSMNet();
      setLayout(null);
      backgroundRoom = Toolkit.getDefaultToolkit().getImage(BG_ROOM);
      
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
      String splitMsg[];
      splitMsg = msg.split(" ");
      if(splitMsg.length < 1)
         return;
      // 채팅 : /MSG 1 할말
      
      // 게임 시작 : /START 4
      if(splitMsg[0].equals("/START")) {
         smNet.setPort(roomNum);
         smNet.setPlayerNum(Integer.parseInt(splitMsg[1]));
         smFrame.sequenceControl("gamePanel", Integer.parseInt(splitMsg[2]));
         GamePanel.rnd = new Random(Long.parseLong(splitMsg[3]));
      }
   }
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(backgroundRoom, 0, 0, getWidth(), getHeight(), this);
   }
}