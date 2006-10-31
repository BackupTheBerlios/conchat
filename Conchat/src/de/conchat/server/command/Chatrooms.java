package de.conchat.server.command;

import java.util.Enumeration;

import de.conchat.server.Command;
import de.conchat.server.domainlogic.Chatroom;

/**
 * @author setcool
 *
 * /chatrooms 
stellt Liste dar
Server:
Schickt die Liste aller Chatrooms an den Client(1) zur�ck


/chatroomlist chatroom1=usernumber|
chatroom2=usernumber

 */
public class Chatrooms extends Command {

	public void addParameter(String parameter) {}

	public String execute() {
		String rooms = "/chatroomlist ";
	
		Enumeration chatrooms =  host.getChatserver().getRooms().elements();
		while (chatrooms.hasMoreElements()) {
			Chatroom room = (Chatroom)chatrooms.nextElement();
			rooms += room.getName() + "=" + room.getUserCount() + "|";
		}
	
		return rooms.substring(0, rooms.length()-1);
	}

	public String getDiscription() {
		return "Returns a well formated list of each chatroom on this server";
	}
}
