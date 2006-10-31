package de.conchat.server.command;

import de.conchat.server.Command;
import de.conchat.server.domainlogic.Chatuser;

/**
 * 
 * @author setcool
 * (nur Buchstaben und Zahlen)
 * /nick <Nick> 
 * 
 * Server:
 * ?berpr?ft, ob der neue Nickname noch frei ist
Pr?fung positiv: Name wird ge?ndert und der Server schickt eine Best?tigung
/connected nickname@chatroom
Pr?fung negativ: Es wird eine Meldung vom Server zur?ckgegeben und der Nickname bleibt unver?ndert
/error nickname in use
/error invalid nickname
 */
public class Nick extends Command {

	private String nick;
	
	public void addParameter(String parameter) {
		// Nur den ersten parameter nehmen
		if (nick == null)
			nick = parameter;
	}

	public String execute() {
		String result = "";
		
		if (nick != null && nick.matches("(\\w|\\d)+")) {
			String oldnick = session.getUser().getName();
			if (session.getUser().rename(nick)) {
				// Alle anderen User des Chatraums ?ber den Namenswechsel informieren
				host.getChatserver().getTcpserver().sendServer2RoomMessage(
					session.getUser().getCurrentChatroom(), "/rename " + oldnick + ":" + nick, new Chatuser
					[] {session.getUser()});
				result = "/connected " + session.getUser().getName() + 
					"@" + session.getUser().getCurrentChatroom().getName();
			} else {
				result = "/error nickname in use";
			}
		} else {
			result = "/error invalid nickname";
		}
		
		return result;
	}

	public String getDiscription() {
		return "Use this command with the nick you want. Be sure that the name you " +
				"type in is a sequence of numbers and chars.";
	}
}
