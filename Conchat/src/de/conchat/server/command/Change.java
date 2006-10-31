package de.conchat.server.command;

import de.conchat.server.Command;
import de.conchat.server.domainlogic.Chatroom;

/**
 * 
 * @author setcool /change <chatroom> <password opt>
 * 
 * Server: Der Client(1) wird aus dem urspr?nglichen Chatroom entfernt. Ist der
 * Chatroom leer, wird er entfernt. Alle Clients des urspr?nglichen Chatrooms
 * werden informiert, dass der Client(1) den Chatroom verlassen hat. Der
 * Client(1) wird dem neuen Chatroom hinzugef?gt, evtl. wird neuer Chatroom
 * erzeugt Der Client(1) erh?lt zur Begr??ung den Connect-Status /connected
 * nickname@chatroom
 * 
 * /error invalid chatroom
 * 
 * password-Vergabe nur bei neuen Chatrooms (privat), beim Betreten eines
 * privaten Chatrooms password erforderlich
 * 
 * /error private chatroom, password required
 * 
 * /error chatroom exists
 * 
 */
public class Change extends Command {

	private String name;

	private String password;

	public void addParameter(String parameter) {
		if (name == null) {
			name = parameter;
		} else {
			password = parameter;
		}
	}

	public String execute() {
		String result = "";

		if (name != null) {
			Chatroom room = host.getChatserver().getChatroom(name);

			// Ist der User schon in dem Chatroom?
			if (session.getUser().getCurrentChatroom() != room) {
				if (room == null) {
					// Neuen Raum erstellen
					if (name.matches("(\\w|\\d)+")) {
						if (password != null) {
							host.getChatserver().createChatroom(name, password);
						} else {
							host.getChatserver().createChatroom(name);
						}
						room = host.getChatserver().getChatroom(name);
						session.getUser().joinToChatroom(room);
						result = "/connected " + session.getUser().getName() + "@"
								+ session.getUser().getCurrentChatroom().getName();
					} else {
						result = "/error invalid chatroom";
					}
				} else {
					// Raum bereits vorhanden
					if (room.hasPassword()) {
						if (password != null && session.getUser().joinToChatroom(room, password)) {
							result = "/connected " + session.getUser().getName() + "@"
									+ session.getUser().getCurrentChatroom().getName();
						} else {
							result = "/error private chatroom, password required";
						}
					} else {
						if (session.getUser().joinToChatroom(room)) {
							result = "/connected " + session.getUser().getName() + "@"
									+ session.getUser().getCurrentChatroom().getName();
						} else {
							result = "/error unable to connect";
						}
					}
				}
			}
		} else {
			result = "/error No correct room";
		}

		return result;
	}
	
	public String getDiscription() {
		return "To change to other Chatroom use /change room, a new room will be created if " +
				"the choosen one doesn't exist. If you want to access a private room type " +
				"in the password as the second parameter.";
	}
}
