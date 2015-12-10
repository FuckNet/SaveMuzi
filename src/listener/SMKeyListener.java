package listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import network.SMNet;

public class SMKeyListener extends KeyAdapter{
	private SMNet net;
	
	private static ArrayList <Integer> keys = new ArrayList <Integer> ();
	
	public static boolean getKeyState(final int keyCode) {
	       
	       if(keys.indexOf(keyCode) != -1) {
	          return true;
	       }
	       else {
	          return false;
	       }
	       
	    }
	   
    public SMKeyListener(SMNet smNet) {
    	this.net = smNet;
    }
    @Override
    public void keyPressed(final KeyEvent e) {
    	String sendStr;
    	if(keys.indexOf(e.getKeyCode()) == -1) {
            keys.add(e.getKeyCode());
        	sendStr = "P " + e.getKeyCode();
        	net.sendToServer(sendStr);
         }
    	
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
    	String sendStr;
    	if(keys.indexOf(e.getKeyCode()) != -1) {
            keys.remove((Integer)e.getKeyCode());
        	sendStr = "R " + e.getKeyCode();
        	net.sendToServer(sendStr);
         }
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
       
    }
 }
