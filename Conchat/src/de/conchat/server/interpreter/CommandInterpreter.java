package de.conchat.server.interpreter;

import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInterpreter {

	private Pattern param = Pattern.compile("(\"[^\"]+?\"|[^ ]+)");
	
	private CommandMapping mapping = null;

	public String parse(String cmd, Hashtable resources) {
		mapping = CommandMapping.getInstance();
		String rueckgabe = "/error command unknown";

		// Wurde etwas eingegeben
		if (!cmd.equals("")) {
			Vector cmdparts = new Vector();
			Matcher mat = param.matcher(cmd);
			
			while (mat.find()) {
				cmdparts.add(mat.group(1).replace("\"", ""));
			}
			
			ICommand command = mapping.getCommand(resources, (String)cmdparts.get(0));

			if (command != null) {
				if (cmdparts.size() > 1) {
					for (int i = 1; i < cmdparts.size(); i++) {
						command.addParameter((String)cmdparts.get(i));
					}
				}
				if (command != null)
					rueckgabe = command.execute();
			}
		}

		return rueckgabe;
	}
}
