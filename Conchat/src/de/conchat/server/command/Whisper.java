package de.conchat.server.command;

import de.conchat.server.Command;
import de.conchat.server.domainlogic.Chatuser;

/**
 * 
 * @author setcool
 * /w <nick> <msg>
 * 
 * Server:
 * Dem Client wird ?bermittelt, dass er angefl?stert wird.

/error Nickname unknown
 */
public class Whisper extends Command {

	private String nick = null;
	
	private String msg = null;
	
	public void addParameter(String parameter) {
		if (nick == null) {
			nick = parameter;
		} else {
			msg = parameter;
		}
	}

	public String execute() {
		String result = "/error nickname unknown";
		
		Chatuser user = Chatuser.getUser(nick);
		
		if (user != null) {
			session.getUser().wisper(user, msg);
			result = "Wisper: " + session.getUser().getName() + ": " + msg;
		}
		
		return result;
	}

	public String getDiscription() {
		return "If you want to tell only special users use whisper to tell them messages secretly.";
	}
}
