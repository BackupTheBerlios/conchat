package de.conchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientSocket extends Thread {

	private boolean isRunning;
	private ChatControl control;
	private Socket socket;
	private BufferedReader incoming;
	private PrintStream outgoing;
	
	public ClientSocket(ChatControl control) {
		this.control = control;
		this.isRunning = false;
		try {
			this.socket = new Socket(this.control.getIP(), this.control.getPort());
			this.incoming = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.outgoing = new PrintStream(this.socket.getOutputStream());
			this.isRunning = true;
			this.start();
		} catch (UnknownHostException e) {
			this.control.showErrorMessage("unbekannter Host");
			this.control.disconnect();
		} catch (IOException e) {
			this.control.showErrorMessage("Fehler beim Senden");
			this.control.disconnect();
		}
	}
	
	public void run() {
		while(this.isRunning) {
			try {
				if (this.socket.isConnected())
					this.control.receiveCommand(this.incoming.readLine());
				else
					throw new SocketException();
			} catch(SocketException ex){
				this.control.showErrorMessage("Verbindung verloren");
				this.isRunning = false;
				this.control.disconnect();
			} catch(NullPointerException ex){
				this.control.showErrorMessage("fehlerhafte Daten");
				control.disconnect();
			} catch (IOException e) {
				this.control.showErrorMessage("Fehler beim Empfangen");
				this.control.disconnect();
			}
		}
	}
	
	public void sendCommand(String cmd) {
		if (this.isRunning)
			this.outgoing.println(cmd);
	}
	
	public boolean isConnected() {
		return this.isRunning;
	}
	
	public void disconnect() {
		try {
			this.socket.close();
			this.isRunning = false;
		} catch (IOException e) {
			this.control.showErrorMessage("Eingabefehler");
		}
	}
}