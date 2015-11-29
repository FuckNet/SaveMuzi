//package game;
//import java.awt.Graphics;
//
//import javax.swing.JPanel;
//
//public class GameScreen extends JPanel{
//	private GamePanel gamePanel;
//	private SMThread mainWork;
//	
//	private Player p[];
//	
//	//private Graphics gc;
//	
//	public GameScreen(GamePanel gamePanel) {
//		this.gamePanel = gamePanel;
//		p = new Player[2];
//		p[0] = new Player();
//		p[0].setLocation(50, getHeight() / 3);
//		add(p[0]);
//
//		p[1] = new Player();
//		p[1].setLocation(50, getHeight()*2 / 3);
//		add(p[1]);
//	}
//	
//	public void setThread(SMThread smThread) {
//		this.mainWork = smThread;
//	}
//	
////	@Override
////	protected void paintChildren(Graphics g) {
////		super.paintChildren(g);
////	}
//}
