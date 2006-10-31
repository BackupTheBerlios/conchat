package de.conchat.server.interpreter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class CommandMapping {

	private static CommandMapping singelton = null;

	private Hashtable commands;

	private String pluginFiles;

	private CommandMapping(Hashtable commands, String pluginFiles) {
		this.commands = commands;
		this.pluginFiles = pluginFiles;
	}

	public static void createCommandMapping(Hashtable commands,
			String pluginFiles) {
		singelton = new CommandMapping(commands, pluginFiles);
	}

	public ICommand getCommand(Hashtable resources, String name) {
		ICommand cmd = null;

		try {
			// CommandObj erstellen
			cmd = getCommandObject(name);

			if (cmd != null)
				// Resourcen hinzufuegen
				setResources(resources, cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cmd;
	}

	private ICommand getCommandObject(String name)
			throws FileNotFoundException, IOException, Exception,
			InstantiationException, IllegalAccessException {
		ICommand cmd = null;
		String cl = (String) commands.get(name.toLowerCase());
		if (cl != null) {
			try {
				if (cl.matches(".*\\.py")) {
					// Python-Klassen:
					cmd = new PythonBinding(pluginFiles, cl);
				} else {
					// Java-Klassen:
					cmd = (ICommand) Class.forName(cl).newInstance();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return cmd;
	}

	public Dictionary getCommands()
			throws FileNotFoundException, IOException, Exception,
			InstantiationException, IllegalAccessException {
		Dictionary arrList = new Hashtable();

		CommandMapping mapping = CommandMapping.getInstance();
		Hashtable mappingtab = mapping.getCommandTable();

		Enumeration keys = mappingtab.keys();
		while (keys.hasMoreElements()) {
			String cmdname = (String) keys.nextElement();
			arrList.put(cmdname, getCommandObject(cmdname));
		}

		return arrList;
	}

	public Hashtable getCommandTable() {
		return commands;
	}

	public static CommandMapping getInstance() {
		return singelton;
	}

	public void setResources(Hashtable resources, ICommand cmd) {
		Enumeration keys = resources.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			cmd.addResource(key, resources.get(key));
		}
	}
}
