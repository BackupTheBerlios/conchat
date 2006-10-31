package de.conchat.server.domainlogic;

import java.util.Enumeration;
import java.util.Vector;

public class Server {

	private Vector rooms;
	
	private ITcpChatServer tcpserver;
	
	private long guestNr = 1;
	
	private Chatroom defaultChatroom;
	
	public Server(ITcpChatServer tcpserver) {
		this.rooms = new Vector();
		
		this.tcpserver = tcpserver;
		
		this.defaultChatroom = new Chatroom("home", this);
		this.rooms.add(this.defaultChatroom);
	}
	
	public void createChatroom(String name) {
		this.rooms.add(new Chatroom(name, this));
	}
	
	public Vector getRooms() {
		return rooms;
	}
	
	public Chatroom getChatroom(String name) {
		Chatroom room = null;
		
		Enumeration e = rooms.elements();
		while (e.hasMoreElements() && room == null) {
			Chatroom tmproom = (Chatroom)e.nextElement();
			if (tmproom.getName().equals(name)) {
				room = tmproom;
			}
		}
		
		return room;
	}
	
	public void unregisterRoom(Chatroom room) {
		if (!room.getName().equals("home")) {
			this.rooms.remove(room);
		}
	}
	
	public void createChatroom(String name, String password) {
		this.rooms.add(new Chatroom(name, password, this));
	}
	
	public boolean containNick(String nick) {
		boolean contains = false;
		
		Enumeration e = rooms.elements();
		while (e.hasMoreElements() && contains == false) {
			Chatroom room = (Chatroom)e.nextElement();
			
			contains = room.containNick(nick);
		}
		
		return contains;
	}
	
	public ITcpChatServer getTcpserver() {
		return tcpserver;
	}
	
	public String getNextGuestNick() {
		return "Guest" + this.guestNr++; 
	}
	
	public Chatroom getDefaultChatroom() {
		return defaultChatroom;
	}
}
