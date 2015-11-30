package listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import network.SMNet;

public class SMKeyListener extends KeyAdapter{
	private SMNet net;
	   
    public SMKeyListener(SMNet smNet) {
    	this.net = smNet;
    }
    @Override
    public void keyPressed(final KeyEvent e) {
    	String sendStr;
    	sendStr = "P " + e.getKeyCode();
    	net.sendToServer(sendStr);
    	
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
    	String sendStr;
    	sendStr = "R " + e.getKeyCode();
    	net.sendToServer(sendStr);
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
       
    }
 }