package de.conchat.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.gnu.glade.GladeXMLException;
import org.gnu.glade.LibGlade;
import org.gnu.gtk.CellRenderer;
import org.gnu.gtk.CellRendererText;
import org.gnu.gtk.DataColumn;
import org.gnu.gtk.DataColumnString;
import org.gnu.gtk.Entry;
import org.gnu.gtk.Gtk;
import org.gnu.gtk.Label;
import org.gnu.gtk.ListStore;
import org.gnu.gtk.SelectionMode;
import org.gnu.gtk.TextBuffer;
import org.gnu.gtk.TextView;
import org.gnu.gtk.TreeIter;
import org.gnu.gtk.TreeView;
import org.gnu.gtk.TreeViewColumn;
import org.gnu.gtk.Window;
import org.gnu.gtk.event.GtkEvent;

public class ChatClient implements ChatView {

	private LibGlade lib;

	private ChatControl control;

	private Entry textLine;

	private TextView chatText;

	private TextBuffer textBuffer;

	private TreeView userList;

	private Window connect;

	private Label statusText;

	private Entry port;

	private Entry hostname;

	private Entry username;
	
	private DataColumnString nameColum;
	
	private ListStore list;

	public ChatClient() throws GladeXMLException, FileNotFoundException,
			IOException {
		lib = new LibGlade("data/glade/conchat-client.glade", this);

		connect = (Window) lib.getWidget("connect");

		statusText = (Label) lib.getWidget("status_text");

		textLine = (Entry) lib.getWidget("text_line");


		nameColum = new DataColumnString();
		list = new ListStore(new DataColumn[] { nameColum });
		userList = (TreeView) lib.getWidget("user_list");
		userList.setAlternateRowColor(true);
		userList.getSelection().setMode(SelectionMode.BROWSE);
		userList.setModel(list);

		TreeViewColumn column = new TreeViewColumn();
		column.setTitle("Chatter");
		column.setResizable(true);
		column.setReorderable(true);
		column.setClickable(true);
		userList.appendColumn(column);
		CellRenderer renderer = new CellRendererText();
		column.packStart(renderer, true);
		column.addAttributeMapping(renderer, CellRendererText.Attribute.TEXT,
				nameColum);
		userList.show();

		chatText = (TextView) lib.getWidget("chat_text");
		textBuffer = new TextBuffer();

		port = (Entry) lib.getWidget("port");

		hostname = (Entry) lib.getWidget("hostname");

		username = (Entry) lib.getWidget("username");

		control = new ChatControl(this);
	}

	public void on_send_text_line(GtkEvent event) {
		String cmd = textLine.getText();
		control.sendCommand(cmd);
	}

	public void on_cancel_connect(GtkEvent event) {
		connect.hide();
	}

	public void on_show_connect(GtkEvent event) {
		connect.show();
	}

	public void on_connect(GtkEvent event) {
		String inetAddr = hostname.getText();
		int portNumber = Integer.parseInt(port.getText());
		control.connect(inetAddr, portNumber);
		control.setUsername(username.getText());
		connect.hide();
	}

	public void on_disconnect(GtkEvent event) {
		control.disconnect();
	}

	public void on_close(GtkEvent event) {
		Gtk.mainQuit();
		System.exit(0);
	}

	public void addText(String text) {
		textBuffer.insertText(text + "\n");
		chatText.setBuffer(textBuffer);
		chatText.draw();
	}

	public synchronized void addUser(String user) {

	}

	public void updateRoomName(String name) {
		// TODO Auto-generated method stub
	}

	public void updateUserName(String name) {
		// TODO Auto-generated method stub

	}

	public void removeUser(String user) {
	}

	public void clearText() {
		textBuffer.setText("");
	}

	public void enableComponents(boolean b) {
		// TODO Auto-generated method stub

	}

	public String getEingabe() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBeenden(String string) {
		// TODO Auto-generated method stub

	}

	public void setInput(Object object) {
		// TODO Auto-generated method stub

	}

	public void setStatus(String string, boolean b, boolean c) {
		statusText.setText(string);
	}

	public void setUserlist(Vector users) {
		Enumeration enumUser = users.elements();
		while (enumUser.hasMoreElements()) {
			String user = (String) enumUser.nextElement();

			TreeIter iter = list.appendRow();
			list.setValue(iter, nameColum, user);
		}

		userList.draw();
	}

	public void setUsernameAndChatroom(String user, String username,
			String chatroom) {
		// TODO Auto-generated method stub

	}

	public void showChatrooms(Hashtable chatrooms) {
		// TODO Auto-generated method stub

	}

	public void showHelp(Hashtable commands) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Gtk.init(args);

		try {
			new ChatClient();
		} catch (GladeXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Gtk.main();
	}

}
