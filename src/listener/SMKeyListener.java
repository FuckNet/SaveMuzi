package listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SMKeyListener extends KeyAdapter{
	   
    private static ArrayList <Integer> keys = new ArrayList <Integer> ();
    
    public static boolean getKeyState(final int keyCode) {
       
       if(keys.indexOf(keyCode) != -1) {
          return true;
       }
       else {
          return false;
       }
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
       if(keys.indexOf(e.getKeyCode()) == -1) {
          keys.add(e.getKeyCode());
       }
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
       if(keys.indexOf(e.getKeyCode()) != -1) {
          keys.remove((Integer)e.getKeyCode());
       }
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
       
    }
 }