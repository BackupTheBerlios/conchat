package de.conchat.server.domainlogic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Vector;

public class Chatroom {
	
	private String name;
	
	private String password;
	
	private Server server;
	
	private Vector nicks;
	
	public Chatroom(String name, Server server) {
		this.name = name;
		this.server = server;
		this.nicks = new Vector();
	}
	
	public Chatroom(String name, String password, Server server) {
		this(name, server);
		this.password = password;
	}
	
	public boolean containNick(String nick) {
		boolean contains = false;
		
		Enumeration e = nicks.elements();
		while (e.hasMoreElements() && contains == false) {
			Chatuser nickObj = (Chatuser)e.nextElement();
			
			contains = nickObj.getName().equals(nick);
		}
		
		return contains;
	}
	
	public Enumeration getNickInRoom() {
		return nicks.elements();
	}
	
	public boolean hasPassword() {
		return password != null;
	}

	public boolean join(Chatuser nick, String password) {
		boolean joined = false;
		
		if (this.password.equals(password) ||
			hashPassword(this.password).equals(password)) {
			this.nicks.add(nick);
			joined = true;
		}
		
		return joined;
	}
	
	public boolean join(Chatuser nick) {
		boolean joined = true;
		
		this.nicks.add(nick);
		joined = true;
		
		return joined;
	}
	
	public String getName() {
		return name;
	}
	
	public void leave (Chatuser nick) {
		this.nicks.remove(nick);
		
		// Wenn der letze den Raum verlaesst den Raum loeschen
		server.unregisterRoom(this);
	}
	
	public int getUserCount() {
		return nicks.size();
	}
	
	public static String hashPassword(String pass) {
		String hash = "";
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(pass.getBytes());
		
		byte[] code = md.digest();
		for (int i = 0; i < code.length; i++) {
			hash += Integer.toHexString(code[i] & 0xff);  
		}
		
		return hash;
	}
	
	public Server getServer() {
		return server;
	}
}
