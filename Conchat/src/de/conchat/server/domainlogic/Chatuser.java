package de.conchat.server.domainlogic;

import java.util.Enumeration;
import java.util.Vector;

public class Chatuser {

	private String name;
	
	private Chatroom currentChatroom;
	
	private static Vector userreg = new Vector();
	
	public Chatuser(String name) {
		this.name = name;
		userreg.add(this);
	}
	
	public Chatroom getCurrentChatroom() {
		return currentChatroom;
	}
	
	public static Chatuser getUser(String nick) {
		Chatuser user = null;
		
		Enumeration users = userreg.elements();
		while (users.hasMoreElements() && user == null) {
			Chatuser tmpuser = (Chatuser)users.nextElement();
			if (tmpuser.getName().equals(nick)) {
				user = tmpuser;
			}
		}
		
		return user;
	}

	public boolean joinToChatroom(Chatroom newChatroom) {
		if (newChatroom.join(this)) {
			this.leaveChatroom();
			this.currentChatroom = newChatroom;
			this.currentChatroom.getServer().getTcpserver().sendServer2RoomMessage(newChatroom, "/enter " + getName() + "@"
					+ getCurrentChatroom().getName());
			return true;
		}
		return false;
	}
	
	public boolean joinToChatroom(Chatroom newChatroom, String password) {		
		if (newChatroom.join(this, password)) {
			this.leaveChatroom();
			this.currentChatroom = newChatroom;
			this.currentChatroom.getServer().getTcpserver().sendServer2RoomMessage(newChatroom, "/enter " + getName() + "@"
					+ getCurrentChatroom().getName());
			return true;
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public void leaveChatroom() {
		if (this.currentChatroom != null) {
			this.currentChatroom.getServer().getTcpserver().sendServer2RoomMessage(currentChatroom, "/exit " + getName() + "@"
					+ currentChatroom.getName());
			this.currentChatroom.leave(this);
		}
	}
	
	public boolean rename(String name) {
		boolean result = false;
		
		if (Chatuser.getUser(name) == null) {
			this.name = name;
			result = true;
		}
		
		return result;
	}
	
	public void wisper(Chatuser recp, String text) {
		getCurrentChatroom().getServer().getTcpserver().sendNick2NickMassage(this, recp, "Wisper: " + this.getName() + ": " + text);
	}
	
	public void say(String text) {
		getCurrentChatroom().getServer().getTcpserver().sendNick2RoomMessage(this, currentChatroom, this.getName() + ": " + text);
	}
	
	public static void removeUser(Chatuser user) {
		userreg.remove(user);
	}
}
