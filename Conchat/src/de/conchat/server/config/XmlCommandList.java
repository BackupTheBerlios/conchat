package de.conchat.server.config;

import java.util.Hashtable;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlCommandList {

	private Hashtable commandList;
	
	public XmlCommandList(NodeList commands) {
		this.commandList = new Hashtable();
		for (int i = 0; i < commands.getLength(); i++) {
			Node command = commands.item(i);
			
			if (command.getNodeName().equals("command")) {
				String path = command.getAttributes().getNamedItem("path").getNodeValue();
				NodeList commandAlias = command.getChildNodes();
				for (int j = 0; j < commandAlias.getLength(); j++) {
					Node alias = commandAlias.item(j);
					
					if (alias.getNodeName().equals("alias")) {
						String aliasname = alias.getAttributes().getNamedItem("name").getNodeValue();
						this.commandList.put(aliasname, path);
					}
				}
			}
		}
	}
	
	public Hashtable getCommandList() {
		return commandList;
	}
}
