package de.conchat.server.command;

import java.util.Dictionary;
import java.util.Enumeration;

import de.conchat.server.Command;
import de.conchat.server.interpreter.CommandMapping;
import de.conchat.server.interpreter.ICommand;

/**
 * @author setcool
 * 
 * Listet alle Kommandos auf, die der Server liefert /helplist
 * commandname=commandtext|commandname=commandtext
 */
public class Help extends Command {

	public void addParameter(String parameter) {
	}

	public String execute() {
		String help = "/helplist ";

		try {
			CommandMapping mapping = CommandMapping.getInstance();
			Dictionary commands = mapping.getCommands();
			Enumeration enu = commands.keys();

			while (enu.hasMoreElements()) {
				String cmdname = (String)enu.nextElement();
				ICommand cmd = (ICommand)commands.get(cmdname);
				help += cmdname + "=" + cmd.getDiscription() + "|";
			}
		} catch (Exception e) {
			help = "Sorry, an server error occurred while creating helplist";
		}

		return help.substring(0, help.length() - 1);
	}

	public String getDiscription() {
		return "Returns a well formated list of information to be able to "
				+ "use commands the way the server can understand.";
	}
}
