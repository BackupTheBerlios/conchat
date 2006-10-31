package de.conchat.server.command;

import de.conchat.server.Command;

/**
 * 
 * @author setcool
 *fragt beim Server nach Connect-Status
 * Server:
 * /connected nickname@chatroom
 */
public class Status extends Command {

	public void addParameter(String parameter) {}

	public String execute() {
		String status = "/connected " + 
			session.getUser().getName() + 
			"@" + 
			session.getUser().getCurrentChatroom().getName();
		
		return status;
	}

	public String getDiscription() {
		return "This command displays the information about your aktive connection." +
				" The format is Username@Chatroom.";
	}
}
