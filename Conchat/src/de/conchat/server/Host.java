package de.conchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import de.conchat.server.domainlogic.Chatroom;
import de.conchat.server.domainlogic.Chatuser;
import de.conchat.server.domainlogic.ITcpChatServer;
import de.conchat.server.domainlogic.Server;

public class Host implements ITcpChatServer {

	private int port = -1;
	
	private Server chatserver = null;
	
	private Vector sessions = null;
	
	private Vector ipBanList = null;
	
	private String adminpass;
	
	private boolean stop;
	
	private ServerSocket ss = null;
	
	public Host(int portst, String adminpass) {
		this.port = portst;
		this.stop = false;
		this.adminpass = adminpass;
		this.chatserver = new Server(this);
		this.sessions = new Vector();
		this.ipBanList = new Vector();
	}
	
	public void start() {
		// Server starten wenn der Port gueltig ist
		if (port != -1) {
			Log.getInstance().writeEntry("Server gestartet");

			try {
				ss = new ServerSocket(port);

				while (!stop) {
					Socket sock = ss.accept();
					
					// Filtern ob der Nutzer gebannt ist
					if (!banned(sock)) {
						Session st = new Session(sock, this);
						st.start();
						this.sessions.add(st);
					} else {
						// gebannte User nicht behandeln
						sock.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				Log.getInstance().writeEntry("Server beendet!");

				try {
					if (ss != null)
						ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean banned(Socket sock) {
		return ipBanList.contains(sock.getInetAddress().getHostAddress());
	}
	
	public void removeSession(Session session) {
		this.sessions.remove(session);
	}
	
	public Server getChatserver() {
		return chatserver;
	}
	
	public void sendNick2NickMassage(Chatuser nicksend, Chatuser nickresv, String msg) {
		Enumeration e = this.sessions.elements();
		
		while (e.hasMoreElements()) {
			Session session = (Session)e.nextElement();
			if (session.getUser() == nickresv) {
				session.sendMsg(msg);
			}
		}
	}
	
	public void sendNick2RoomMessage(Chatuser nick, Chatroom room, String msg) {
		Enumeration e = this.sessions.elements();
		
		while (e.hasMoreElements()) {
			Session session = (Session)e.nextElement();
			if (session.getUser().getCurrentChatroom() == room) {
				session.sendMsg(msg);
			}
		}
	}
	
	public void sendServer2RoomMessage(Chatroom room, String msg) {
		Enumeration e = this.sessions.elements();
		
		while (e.hasMoreElements()) {
			Session session = (Session)e.nextElement();
			if (session.getUser().getCurrentChatroom() == room) {
				session.sendMsg(msg);
			}
		}
	}
	
	public void sendServer2RoomMessage(Chatroom room, String msg, Chatuser[] leaveOut) {
		Enumeration e = this.sessions.elements();
		
		while (e.hasMoreElements()) {
			Session session = (Session)e.nextElement();
			boolean leave = false;
			for (int user = 0; user < leaveOut.length; user++) {
				if (leaveOut[user] == session.getUser()) {
					leave = true;
				}
			}
			if (session.getUser().getCurrentChatroom() == room && !leave) {
				session.sendMsg(msg);
			}
		}
	}
	
	public boolean isAdminPassword(String password) {
		return password.equals(adminpass);
	}

	public void stop() {
		this.stop = true;
		try {this.ss.close();}catch(Exception e) {}
	}
	
	public Collection getSessions() {
		return sessions;
	}
	
	public Session getUserSession(String username) {
		// Suchen nach einer beutzersession
		Session session = null;
			
		Enumeration e = this.sessions.elements();
		
		while (e.hasMoreElements()) {
			Session tmpSession = (Session)e.nextElement();
			
			if (tmpSession.getUser().getName().equals(username)) {
				session = tmpSession;
				break;
			}
		}
		
		return session;
	}
	
	public Collection getIpBanList() {
		return ipBanList;
	}
	
	public boolean kick(String username) {
		boolean kicked = false;
		
		if (getUserSession(username) != null)
			getUserSession(username).close();
		
		return kicked;
	}
	
	public boolean ban(String username) {
		boolean ban = false;
		
		Session usess = getUserSession(username);
		
		// Wenn eine Benutzersesion existiert
		if (usess != null) {
			ipBanList.add(usess.getIp());
			ban = true & kick(username);
		}
			
		return ban;
	}
	
	public boolean unban(String ip) {
		boolean ipexist = false;
		
		if (ipBanList.contains(ip)) {
			ipBanList.remove(ip);
			ipexist = true;
		}
		
		return ipexist;
	}
}
