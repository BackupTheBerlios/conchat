package de.conchat.client;

import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatControl {

	/***************************************************************************
	 * Attribute der Klasse ChatControl *
	 **************************************************************************/
	private ChatView view;

	private ClientSocket socket;

	private String ip;

	private int port;

	private String username;

	private String chatroom;

	private boolean isDisconnectReleased;

	/***************************************************************************
	 * Konstruktor der Klasse ChatControl *
	 **************************************************************************/
	public ChatControl(ChatView view) {
		this.view = view;
		this.username = "";
		this.chatroom = "";
		this.isDisconnectReleased = false;
	}

	public String getIP() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}

	/***************************************************************************
	 * Senden und Empfangen der Kommandos *
	 **************************************************************************/
	public void receiveCommand(String cmd) {
		if (!"".equals(cmd) && cmd != null) {

			if (cmd.matches("^\\/error .*$")) {
				this.showErrorMessage(cmd.replaceFirst("^/error ", ""));

			} else if (cmd.matches("^/userexit$")) {
				this.disconnect();

			} else if (cmd
					.matches("^\\/helplist (((\\/[- \\w]+)=([^|]+))(\\|?))+$")) {
				cmd = cmd.replaceFirst("^\\/helplist ", "");
				Pattern p = Pattern.compile("((\\/[- \\w]+)=([^|]+))");
				Matcher m = p.matcher(cmd);
				Hashtable commands = new Hashtable();
				while (m.find())
					commands.put(m.group(2), m.group(3));
				this.view.showHelp(commands);

			} else if (cmd
					.matches("^\\/connected ([a-zA-Z0-9]+)@([a-zA-Z0-9]+)$")) {
				String chat = this.chatroom;
				String user = this.username;
				Pattern p = Pattern.compile("([a-zA-Z0-9]+)@([a-zA-Z0-9]+)");
				Matcher m = p.matcher(cmd);
				if (m.find()) {
					this.username = m.group(1);
					this.chatroom = m.group(2);
				}
				this.view.setUsernameAndChatroom(user, this.username,
						this.chatroom);
				if (!this.chatroom.equals(chat)) {
					this.clearContent();
					this.sendCommand("/users");
				}

			} else if (cmd
					.matches("^\\/chatroomlist ((([- \\/\\w]+)=(\\d+))(\\|?))+$")) {
				cmd = cmd.replaceFirst("^\\/chatroomlist ", "");
				Pattern p = Pattern.compile("(([- \\/\\w]+)=(\\d+))");
				Matcher m = p.matcher(cmd);
				Hashtable chatrooms = new Hashtable();
				while (m.find())
					chatrooms.put(m.group(2), m.group(3));
				this.view.showChatrooms(chatrooms);

			} else if (cmd.matches("^\\/userlist (([- \\/\\w]+)(\\|?))+$")) {
				Vector user = new Vector();
				cmd = cmd.replaceFirst("^\\/userlist ", "");
				Pattern p = Pattern.compile("([- \\/\\w]+)");
				Matcher m = p.matcher(cmd);
				while (m.find())
					user.add(m.group(1));
				this.view.setUserlist(user);

			} else if (cmd.matches("^\\/exit ([a-zA-Z0-9]+)@([a-zA-Z0-9]+)$")) {
				Pattern p = Pattern.compile("([a-zA-Z0-9]+)@([a-zA-Z0-9]+)");
				Matcher m = p.matcher(cmd);
				if (m.find())
					if (this.chatroom.equals(m.group(2))) {
						String user = m.group(1);
						this.view.removeUser(user);
						this.view.addText(user + " hat den Raum verlassen.");
					}

			} else if (cmd.matches("^\\/enter ([a-zA-Z0-9]+)@([a-zA-Z0-9]+)$")) {
				Pattern p = Pattern.compile("([a-zA-Z0-9]+)@([a-zA-Z0-9]+)");
				Matcher m = p.matcher(cmd);
				if (m.find())
					if (this.chatroom.equals(m.group(2))) {
						String user = m.group(1);
						this.view.addUser(user);
						this.view.addText(user + " hat den Raum betreten.");
					}
			} else {
				this.view.addText(cmd);
			}

		} else if (cmd == null) {
			this.disconnect();
			this.showErrorMessage("Verbindungsabbruch");
		}
	}

	public void sendCommand(String cmd) {
		if (this.socket.isConnected())
			this.socket.sendCommand(cmd);
	}

	/***************************************************************************
	 * Verarbeiten der Kommandos *
	 **************************************************************************/
	public void showErrorMessage(String msg) {
		if (!this.isDisconnectReleased)
			this.view.setStatus("Fehler: " + msg, true, true);
	}

	public void connect(String inetAddr, int port) {
		if (this.socket == null || this.socket.isConnected())
			this.disconnect();
		this.ip = inetAddr;
		this.port = port;
		this.socket = new ClientSocket(this);

		if (this.socket.isConnected()) {
			this.view.enableComponents(true);
			this.sendCommand("/users");
			this.view.setStatus("Verbindung wurde hergestellt!", false, false);
		} else {
			this.showErrorMessage("Verbindungsabbruch");
		}
	}

	public void disconnect() {
		this.view.enableComponents(false);
		if (this.socket != null && this.socket.isConnected())
			this.socket.disconnect();
		if (this.isDisconnectReleased)
			this.view.setStatus("Verbindung wurde beendet!", false, false);
		this.isDisconnectReleased = false;
	}

	private void clearContent() {
		this.view.clearText();
	}
	
	public void setUsername(String username) {
		this.username = username;
		sendCommand("/nick " + username);
	}

	public void changeRoom(String room) {
		if (room.matches("[^ ]+ [^ ]")) {
			String[] word = room.split(" ");
			this.sendCommand("/change " + word[0] + " " + word[1]);
			view.updateRoomName(word[0]);
		} else {
			this.sendCommand("/change " + room);
			view.updateRoomName(room);
		}
	}

	public String getMyUsername() {
		return this.username;
	}
}