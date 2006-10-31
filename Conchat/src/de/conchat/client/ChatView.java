package de.conchat.client;

import java.util.Hashtable;
import java.util.Vector;

public interface ChatView {

	void showHelp(Hashtable commands);

	void setUsernameAndChatroom(String user, String username, String chatroom);

	void showChatrooms(Hashtable chatrooms);

	void setUserlist(Vector users);

	void removeUser(String user);

	void addText(String string);

	void addUser(String user);

	void setBeenden(String string);

	void setStatus(String string, boolean b, boolean c);

	void enableComponents(boolean b);

	void setInput(Object object);

	void clearText();

	String getEingabe();

	void updateUserName(String name);
	
	void updateRoomName(String name);
}