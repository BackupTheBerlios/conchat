package de.conchat.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {

	private static Log log;

	BufferedWriter filebuf;

	private Log() {
		try {
			FileWriter file = new FileWriter("chatserver.log");
			filebuf = new BufferedWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Log getInstance() {
		if (log == null)
			log = new Log();
		return log;
	}

	public void writeEntry(String logline) {
		Date datum = new Date();
		try {
			filebuf.write("[" + datum + "] " + logline + "\n");
			System.out.println("[" + datum + "] " + logline);
			filebuf.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
