package de.conchat.server.command;

import de.conchat.server.Command;

/**
 * 
 * @author setcool
 * 
 * /disconnect Schliesst die Verbindung mit dem clienten
 */
public class Disconnect extends Command {

	public void addParameter(String parameter) {
	}

	public String execute() {
		session.close();
		return "/userexit";
	}

	public String getDiscription() {
		return "Disconnect from the Server. The Connection will be closed.";
	}
}
