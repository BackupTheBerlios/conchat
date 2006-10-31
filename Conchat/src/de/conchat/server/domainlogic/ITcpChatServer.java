package de.conchat.server.domainlogic;

public interface ITcpChatServer {
	
	public void sendNick2NickMassage(Chatuser nicksend, Chatuser nickresv, String msg);

	public void sendNick2RoomMessage(Chatuser nick, Chatroom room, String msg);

	public void sendServer2RoomMessage(Chatroom room, String msg);
	
	public void sendServer2RoomMessage(Chatroom room, String msg, Chatuser [] leaveOut);
}
