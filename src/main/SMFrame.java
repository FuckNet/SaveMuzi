package main;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import game.GamePanel;
import home.HomePanel;
import lobby.LobbyPanel;
import network.SMNet;
import room.RoomPanel;

//기본 윈도우를 형성하는 프레임을 만든다
public class SMFrame extends JFrame {
   public static final int SCREEN_WIDTH = 1024, SCREEN_HEIGHT = 768;

   private SMNet smNet;
   private CardLayout layoutManager;
   private Container contentPane;

   private HomePanel homePanel;
   private LobbyPanel lobbyPanel;
   public RoomPanel roomPanel;
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
      smNet.setHomePanel(homePanel);
      smNet.setLobbyPanel(lobbyPanel);
      smNet.setRoomPanel(roomPanel);
      smNet.setGamePanel(gamePanel);
      smNet.toHomePanel();
      smNet.network();

      contentPane = this.getContentPane();
      contentPane.add("homePanel", homePanel);
      contentPane.add("lobbyPanel", lobbyPanel);
      contentPane.add("roomPanel", roomPanel);
      contentPane.add("gamePanel", gamePanel);

      setVisible(true);
      sequenceControl("homePanel", 0);

      homePanel.start();
   }

   public void sequenceControl(String panelName, int arg0) {
      // arg0는 패널마다 의미하는 바가 다름
      switch (panelName) {
      case "homePanel":
         if (arg0 == 1) { // 로그아웃 버튼으로 홈으로 온 경우
            homePanel.resetAutoLogin();
         }
         smNet.toHomePanel();
         smNet.setStateToHome();
         changePanel(panelName);
         break;
      case "lobbyPanel":
         smNet.toLobbyPanel();
         smNet.setStateToLobby();
         for (int i = 0; i < arg0; i++)
            lobbyPanel.addRoom();
         changePanel(panelName);
         repaint();
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
      switch (panelName) {
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
   
   class ExitListener implements WindowListener {
      @Override
      public void windowClosing(WindowEvent e) {
         //smNet.sendMSGTCP("/EXIT");
         System.out.println("프로그램 종료");
         System.exit(0);
      }

      @Override
      public void windowActivated(WindowEvent arg0) { }
      @Override
      public void windowClosed(WindowEvent arg0) { }
      @Override
      public void windowDeactivated(WindowEvent arg0) { }
      @Override
      public void windowDeiconified(WindowEvent arg0) { }
      @Override
      public void windowIconified(WindowEvent arg0) { }
      @Override
      public void windowOpened(WindowEvent arg0) { }
   }
}