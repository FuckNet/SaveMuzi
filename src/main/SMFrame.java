package main;
import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JFrame;

import game.GamePanel;
import home.HomePanel;
import lobby.LobbyPanel;
import room.RoomPanel;

//기본 윈도우를 형성하는 프레임을 만든다
public class SMFrame extends JFrame{
	private final int SCREEN_WIDTH = 1024, SCREEN_HEIGHT = 768;
	
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
		
		homePanel = new HomePanel(this);
		lobbyPanel = new LobbyPanel(this);
		roomPanel = new RoomPanel(this);
		gamePanel = new GamePanel(this);
		
		contentPane = this.getContentPane();
		contentPane.add("homePanel", homePanel);
		contentPane.add("lobbyPanel", lobbyPanel);
		contentPane.add("roomPanel", roomPanel);
		contentPane.add("gamePanel", gamePanel);

		setVisible(true);
		changePanel("homePanel");
		
		homePanel.start();
	}
	
	public void SequenceControl() {
		
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
	
}
