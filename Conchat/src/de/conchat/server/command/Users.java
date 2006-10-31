package de.conchat.server.command;

import java.util.Enumeration;

import de.conchat.server.Command;
import de.conchat.server.domainlogic.Chatuser;

/**
 * 
 * @author setcool
 *
 * /users
stellt Liste dar
Server:
Schickt eine Liste aller im aktuellen Chatroom befindlichen User an Client(1)

/userlist nick1|nick2|nick

 */
public class Users extends Command {

	public void addParameter(String parameter) {}
	
	public String execute() {
		String usersstr = "/userlist ";
		
		Enumeration users = session.getUser().
			getCurrentChatroom().getNickInRoom();
		while (users.hasMoreElements()) {
			Chatuser user = (Chatuser)users.nextElement();
			usersstr += user.getName() + "|";
		}
		
		return usersstr.substring(0, usersstr.length()-1);
	}

	public String getDiscription() {
		return "Returns you a well formed list of each chatuser in your room.";
	}
}
