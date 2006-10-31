package de.conchat.server.config;

import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ChatserverConfig {
	
	private XmlCommandList xcl;
	
	public ChatserverConfig() {
	}
	
	public void openXml(String path) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(path);
			
			Node root = doc.getFirstChild();
			
			NodeList usersCommands = root.getChildNodes();
			for (int i = 0; i < usersCommands.getLength(); i++) {
				Node uc = usersCommands.item(i);
				
				if (uc.getNodeName().equals("commands")) {
					xcl = new XmlCommandList(uc.getChildNodes());
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public Hashtable getCommands() {
		return xcl.getCommandList();
	}
}
