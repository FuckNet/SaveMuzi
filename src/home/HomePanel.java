package home;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.SMFrame;
import network.SMNet;

public class HomePanel extends JPanel{
   private static final String BG_HOME = "res/background/backgroundHome.png";
   private static final String LOGO = "res/logo/logo.png";
   private static final String PUSHBAR = "res/foreground/pushSpace.png";
   
   private SMNet smNet;
   private SMFrame smFrame;
   private Image backgroundHome;
   private Image pushBarImage;
   
   private LoginPanel loginPanel;
   private SignUpPanel signUpPanel;
   
   private JLabel pushBarLabel;
   private JLabel logoLabel;
   
   public HomePanel(SMFrame smFrame) {
      setLayout(null);
      this.smFrame = smFrame;
      loginPanel = new LoginPanel();
      loginPanel.setLocation((smFrame.getWidth() - loginPanel.getWidth()) / 2 ,
            (smFrame.getHeight() - loginPanel.getHeight())* 3 / 5);
      signUpPanel = new SignUpPanel();
      signUpPanel.setLocation((smFrame.getWidth() - loginPanel.getWidth()) / 2 ,
            (smFrame.getHeight() - loginPanel.getHeight())* 3 / 5);
      
      smNet = smFrame.getSMNet();
      
      try {
         Thread.sleep(200);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      backgroundHome = Toolkit.getDefaultToolkit().getImage(BG_HOME);
      ImageIcon pushBarIcon = new ImageIcon(PUSHBAR);
      pushBarLabel = new JLabel();
      pushBarLabel.setSize(500, 100);
      pushBarLabel.setLocation((smFrame.getWidth() - pushBarLabel.getWidth()) / 2,
            (smFrame.getHeight() - pushBarLabel.getHeight())*4 / 5);
      pushBarImage = pushBarIcon.getImage();
      Image pushBarImage2 = pushBarImage.getScaledInstance(pushBarLabel.getWidth(), pushBarLabel.getHeight(),  java.awt.Image.SCALE_SMOOTH);
      pushBarIcon = new ImageIcon(pushBarImage2);
      pushBarLabel.setIcon(pushBarIcon);
      
      logoLabel = new JLabel();
      logoLabel.setSize(600, 100);
      logoLabel.setLocation((smFrame.getWidth() - logoLabel.getWidth()) / 2,
            (smFrame.getHeight() - logoLabel.getHeight())*1 / 10);
      
      logoLabel.setIcon(new ImageIcon(LOGO));
      add(logoLabel);
      addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            remove(pushBarLabel);
            showLogin();
            
         }
      });
      
      
   }
   // Space Bar 입력을 받을 때 Login창을 띄운다.
   public void start() {

      while(true) {
         try {
            add(pushBarLabel);
            for(int i = 0; i < 15; i++) {   // sleep 도중 키 이벤트 발생이 안되는 것을 방지하기 위해 잘게 쪼갬
               Thread.sleep(10);
            }
            repaint();
            remove(pushBarLabel);
            for(int i = 0; i < 15; i++) {
               Thread.sleep(10);
            }
            repaint();
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   private void showLogin() {
//      add(loginPanel);
//      repaint();
      smFrame.sequenceControl("lobbyPanel", 0);
      //smFrame.changePanel("gamePanel");
   }
   private void showSignup() {
   
   }
   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(backgroundHome, 0, 0, getWidth(), getHeight(), this);
   }
   
}