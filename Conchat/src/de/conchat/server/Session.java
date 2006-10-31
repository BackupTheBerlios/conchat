package de.conchat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Hashtable;

import de.conchat.server.domainlogic.Chatuser;
import de.conchat.server.interpreter.CommandInterpreter;

public class Session extends Thread {

	private boolean closed = false;
	
	private Socket sock = null;
	
	private BufferedReader reader = null;
	
	private BufferedWriter writer = null;
	
	private Hashtable resources = null;
	
	private Chatuser user = null;

	public Session(Socket sock, Host serv) {
		this.sock = sock;
		this.resources = new Hashtable();
		this.resources.put("host", serv);
		this.resources.put("session", this);
		this.user = new Chatuser(serv.getChatserver().getNextGuestNick());
		this.user.joinToChatroom(serv.getChatserver().getDefaultChatroom());
	}
	
	public void close() {
		closed = true;
	}

	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(sock
					.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(sock
					.getOutputStream()));

			Log.getInstance().writeEntry(getIp() + " Client verbunden!");
			
			// Parser erzeugen
			CommandInterpreter cmdinter = new CommandInterpreter();

			// Guestnamen und Chatraum schicken
			String status = "/connected " + user.getName() + 
				"@" + user.getCurrentChatroom().getName();
			writer.write(status + "\n");
			writer.flush();
			
			// Befehle auslesen
			String line = null;
			while (((line = reader.readLine()) != null) && (!closed)) {
				Log.getInstance().writeEntry(getIp() + " RECV FROM CLIENT: " + line);
				// Befehl?
				if (line.startsWith("/")) {
					// Befehl parsen
					String response = cmdinter.parse(line, this.resources) + "\n";
					Log.getInstance().writeEntry(getIp() + " SEND TO CLIENT: " + response.replace("\n", "\\n"));
					writer.write(response);
					writer.flush();	
				} else {
					// Chattext an alle user des Raums schicken
					this.user.say(line);
				}
			}
		} catch (IOException e) {
			Log.getInstance().writeEntry(getIp() + " Verbinung wird unsauber geschlossen!");
		} finally {
			try {
				if (sock != null) sock.close();
			} catch (Exception e) {}
			Log.getInstance().writeEntry(getIp() + " Clientverbinung geschlossen!");
		}
		
		// Chatuser entfernen
		this.user.leaveChatroom();
		Chatuser.removeUser(user);
		((Host)this.resources.get("host")).removeSession(this);
	}

	public Chatuser getUser() {
		return user;
	}
	
	public BufferedReader getReader() {
		return reader;
	}
	
	public BufferedWriter getWriter() {
		return writer;
	}
	
	public String getIp() {
		return (sock.getInetAddress() != null) ? sock.getInetAddress().getHostAddress() : "0.0.0.0";
	}
	
	public void sendMsg(String msg) {
		try {
			Log.getInstance().writeEntry(getIp() + " SEND TO CLIENT: " + msg);
			this.writer.write(msg + "\n");
			this.writer.flush();	
		} catch (IOException e) {
			Log.getInstance().writeEntry(getIp() + " FAILED TO SEND");
		}
	}
}