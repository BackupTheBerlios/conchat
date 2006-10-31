package de.conchat.server.interpreter;

public interface ICommand {
	
	public void addResource(String name, Object res);

	public void addParameter(String parameter);
	
	public String getDiscription();
	
	public String execute();
}
