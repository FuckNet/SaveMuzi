package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Vector;

import main.SMFrame;

public class Player {
   
   private static final int PWIDTH = 70, PHEIGHT = 70;
   
   private GamePanel gamePanel;
   private int score;
   private int x;
   private int y;
   protected Point center;
   private int speed;
   private int degree;
   private int mode;
   private int imgIndex;
   private int cnt;
   private int life;
   private int shieldPoint;
   private int power;
   private Vector<Object> bullets;
   private boolean isShoot;
   private boolean inv;
   private Image[] character;
   
   private static SMThread smThread;
   
   static Image shieldImg;
   
   private boolean state[];
   
   public static final int SWIDTH = 64;
   public static final int SHEIGHT = 64;
   
   public static void playerInit(SMThread smThread) {
      shieldImg = Toolkit.getDefaultToolkit().getImage("res/game/shield.png");
      Player.smThread = smThread;
   }
   
   public Player(GamePanel gamePanel) {
      this.gamePanel = gamePanel;
      initData();
      state = new boolean[999];
      for(int i = 0; i < state.length; i++) {
         state[i] = false;
      }
   }
   
   public void initData() {
      score=0;
      x = 0;
      y = 0;
      speed=4;
      degree=-1;
      mode=1;
      imgIndex=2;
      cnt=0;
      life=300;
      bullets = gamePanel.getBullets();
      center = new Point(x/100 + PWIDTH/2, y/100 + PHEIGHT/2);
      isShoot = false;
      shieldPoint = 0;
      power = 1;
      inv = false;
      character = new Image[9];
      //이미지 저장
      for(int i = 0; i < 9; i++) {
         //if(i < 10)
            character[i] = Toolkit.getDefaultToolkit().getImage("res/character/c1_0" + i + ".png");
         
      }
   }
   
   
   public void paint(Graphics g) {
      g.drawImage(character[imgIndex], x/100, y/100, PWIDTH, PHEIGHT, null);
      if(shieldPoint>2)  {
         int disx = (int)(Math.sin(Math.toRadians((smThread.cnt%72)*5))*16+x/100);
         int disy = (int)(Math.cos(Math.toRadians((smThread.cnt%72)*5))*16+y/100);
         int sx = (smThread.cnt/6%7)*64;
         g.drawImage(shieldImg, disx, disy, disx + SWIDTH, disy + SHEIGHT, sx, 0, sx+SWIDTH, SHEIGHT, null);
         //drawImageAnc(shield, (int)(Math.sin(Math.toRadians((smThread.cnt%72)*5))*16+myx), (int)(Math.cos(Math.toRadians((smThread.cnt%72)*5))*16+myy), (main.smThread.cnt/6%7)*64,0, 64,64, 4);//실드 라이프가 3 이상
      }
      else if(shieldPoint>0&&smThread.cnt%4<2) {
         int sx = (smThread.cnt/6%7)*64;
         int disx = (int)(Math.sin(Math.toRadians((smThread.cnt%72)*5))*16+x/100);
         int disy = (int)(Math.cos(Math.toRadians((smThread.cnt%72)*5))*16+y/100);      
         g.drawImage(shieldImg, disx, disy, disx + SWIDTH, disy + SHEIGHT, sx, 0, sx+SWIDTH, SHEIGHT, null);
         //drawImageAnc(shield, (int)(Math.sin(Math.toRadians((smThread.cnt%72)*5))*16+myx), (int)(Math.cos(Math.toRadians((smThread.cnt%72)*5))*16+myy), (main.smThread.cnt/6%7)*64,0, 64,64, 4);//실드 라이프가 1, 2면 점멸
      }
   
   }
   
   public void setState(int index, boolean bool) {
      state[index] = bool;
   }
   
   public void process() {
      imgIndex = 2;
      if(state[KeyEvent.VK_UP] == true)
         y -= speed * 100;
      if(state[KeyEvent.VK_DOWN] == true)
         y += speed * 100;
      if(state[KeyEvent.VK_RIGHT] == true)
         x += speed * 100;
      if(state[KeyEvent.VK_LEFT] == true) {
         x -= speed * 100;
         imgIndex = 4;
      }
      if(state[KeyEvent.VK_SPACE] == true) {
         imgIndex = 6;
         isShoot = true;
      }
      
      switch(mode){
      case 1:
         x+=200;
         if(x>13000) mode=2;
         break;
      case 0:
         if(cnt--==0) {
            mode=2;
            imgIndex=0;
         }
      case 2:
         if(imgIndex == 6) {
            if(smThread.cnt % 4 == 0 && isShoot) {
               isShoot = false;
               Bullet shoot = new Bullet(x+5000, y+4000, 0, 0, 270, 16, power);
               //gamePanel.add(shoot);
               bullets.add(shoot);
            }
         }
         break;
      case 3:
         imgIndex = 8;
         if(cnt-- == 0) {
            mode = 0;
            cnt = 50;
         }
         break;
      }
      if(x<0) x=0;
      if(x>SMFrame.SCREEN_WIDTH * 100-7000) x=SMFrame.SCREEN_WIDTH * 100-7000;
      if(y<3000) y=3000;
      if(y>SMFrame.SCREEN_HEIGHT * 100-10000) y=SMFrame.SCREEN_HEIGHT * 100-10000;
      center.x = x/100 + PWIDTH/2;
      center.y = y/100 + PHEIGHT/2;
   }
   
   public void setDegree(int degree) {
      this.degree = degree;
   }
   public void setImgIndex(int imgIndex) {
      this.imgIndex = imgIndex;
   }
   public void setX(int x) {
      this.x = x * 100;
   }
   public void setY(int y) {
      this.y = y * 100;
   }
   public void setMode(int mode) {
      this.mode = mode;
   }
   public void setCnt(int cnt) {
      this.cnt = cnt;
   }
   public void setLife(int life) {
      this.life = life;
   }
   public void setShield(int shieldPoint) {
      this.shieldPoint = shieldPoint;
   }
   
   public int getX() {
      return this.x/100;
   }
   public int getY() {
      return this.y/100;
   }
   public int getMode() {
      return this.mode;
   }
   public int getShield() {
      return this.shieldPoint;
   }
   public int getLife() {
      return this.life;
   }
   
}