package de.conchat.server;

import de.conchat.server.interpreter.ICommand;

public abstract class Command implements ICommand {
	
	protected Host host = null;
	
	protected Session session = null;

	public void addResource(String name, Object res) {
		if (name.equals("host"))
			host = (Host)res;
		else if (name.equals("session")) {
			session = (Session)res;
		}
	}
}
