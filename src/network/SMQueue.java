package network;

import java.util.ArrayList;

public class SMQueue {
	private ArrayList<String> als;
	private static SMQueue smQueue = new SMQueue();
	
	
	public static SMQueue getSMQueue() {
		return smQueue;
	}
	public SMQueue() {
		als = new ArrayList<String>();
	}
	public synchronized void addMSG(String str) {
		als.add(str);
	}
	
	public synchronized String getMSG() {
		try {
		return als.remove(0);
		}catch(Exception e) {
			return null;
		}
	}
}
