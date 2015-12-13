package main;
import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JFrame;

import game.GamePanel;
import home.HomePanel;
import lobby.LobbyPanel;
import network.SMNet;
import room.RoomPanel;

//�⺻ �����츦 �����ϴ� �������� �����
public class SMFrame extends JFrame{
   public static final int SCREEN_WIDTH = 1024, SCREEN_HEIGHT = 768;
   
   private SMNet smNet;
   private CardLayout layoutManager;
   private Container contentPane;
   
   private HomePanel homePanel;
   private LobbyPanel lobbyPanel;
   private RoomPanel roomPanel;
   private GamePanel gamePanel;
   
   public SMFrame() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      layoutManager = new CardLayout();
      setLayout(layoutManager);
      setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
      setResizable(false);
      
      smNet = new SMNet();
      homePanel = new HomePanel(this);
      lobbyPanel = new LobbyPanel(this);
      roomPanel = new RoomPanel(this);
      gamePanel = new GamePanel(this);
      smNet.setLobbyPanel(lobbyPanel);
      smNet.setRoomPanel(roomPanel);
      smNet.setGamePanel(gamePanel);
      smNet.toLobbyPanel();
      //smNet.toRoomPanel();
      //smNet.toGamePanel();
      smNet.network();
      
      contentPane = this.getContentPane();
      contentPane.add("homePanel", homePanel);
      contentPane.add("lobbyPanel", lobbyPanel);
      contentPane.add("roomPanel", roomPanel);
      contentPane.add("gamePanel", gamePanel);

      setVisible(true);
      changePanel("homePanel");
      
      homePanel.start();
   }
   
   public void sequenceControl(String panelName, int arg0) {
      // arg0�� �гθ��� �ǹ��ϴ� �ٰ� �ٸ�
      switch(panelName) {
      case "homePanel":
         changePanel(panelName);
         break;
      case "lobbyPanel":
         smNet.toLobbyPanel();
         smNet.setStateToLobby();
         changePanel(panelName);
         break;
      case "roomPanel":
         smNet.toRoomPanel();
         smNet.setStateToRoom();
         roomPanel.setRoomNum(arg0);
         changePanel(panelName);
         break;
      case "gamePanel":
         gamePanel.setMaxPlayerNum(arg0);
         gamePanel.gameStart();
         smNet.toGamePanel();
         smNet.setStateToGame();
         changePanel(panelName);
         break;
      }
   }
   
   public void changePanel(String panelName) {
      layoutManager.show(contentPane, panelName);
      switch(panelName) {
      case "homePanel":
         homePanel.requestFocus();
         break;
      case "lobbyPanel":
         lobbyPanel.requestFocus();
         break;
      case "roomPanel":
         roomPanel.requestFocus();
         break;
      case "gamePanel":
         gamePanel.requestFocus();
         break;
      }
   }
   
   public SMNet getSMNet() {
      return this.smNet;
   }
   
}