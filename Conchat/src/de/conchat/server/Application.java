package de.conchat.server;

import java.io.File;
import java.util.Properties;

import org.python.util.PythonInterpreter;

import de.conchat.server.config.ChatserverConfig;
import de.conchat.server.interpreter.CommandMapping;

public class Application {

	/**
	 * Starten des Echo-Servers
	 * Erwidert den ASCII-Text, der ihm geschickt wurde
	 * @param args 1. Parameter ist der Port des Servers
	 */
	public static void main(String[] args) {		
		// Port auslesen ggf. Hilfe ausgeben
		int port = -1;
		if (args.length == 3) {
			try {
				port = Integer.parseInt(args[0]);
				if (port < 0 || port > 65535) {
					System.err.println("Port ist keine gueltige Ganzzahl, zwischen 1 ung 65535");
					port = -1;
				}
			} catch (NumberFormatException e1) {
				System.err.println("Port ist keine gueltige Ganzzahl, zwischen 1 ung 65535");
			}
			
			File file = new File(args[1]);
			if (file.exists()) {
				// Command Mapping
				Log.getInstance().writeEntry("Lade Command Mappings");
				ChatserverConfig config = new ChatserverConfig();
				config.openXml(args[1]);
				CommandMapping.createCommandMapping(config.getCommands(), "data/plugins");
				
				// Python Engine
				Log.getInstance().writeEntry("Starte Python Plugin Engine");
				Properties prop = new Properties();	
				prop.put("python.path", "data/plugins::lib/Lib".replace("::", System.getProperty("path.separator")));
				PythonInterpreter.initialize(System.getProperties(), prop, new String[] {});
				
				// Server starten
				Host serv = new Host(port, args[2]);
				serv.start();
			} else {
				System.err.println("Keine Konfiguration unter \"" + args[1] +"\" gefunden");
			}
		} else {
			System.out.println("usage: " +
				"java Application <Serverport> <Configfile> <adminpass>");
		}
	}
}

