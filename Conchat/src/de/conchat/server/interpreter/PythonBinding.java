package de.conchat.server.interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.python.core.PyJavaInstance;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

public class PythonBinding implements ICommand {

	private static String pluginPattern = "class\\s*(\\w*)\\s*\\(Command\\)\\s*:\\s*";

	private static Pattern pat = Pattern.compile(pluginPattern);

	private PyObject pyPlugin;

	private PythonInterpreter py;

	public PythonBinding(String pluginFiles, String filePath)
			throws FileNotFoundException, IOException, Exception {

		// Classennamen des Plugins evaluieren
		FileInputStream fis = new FileInputStream(pluginFiles + "/" + filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		String className = null;
		while ((line = br.readLine()) != null) {
			if (line.matches(pluginPattern)) {
				Matcher mat = pat.matcher(line);
				if (mat.find())
					className = mat.group(1);
			}
		}
		br.close();

		if (className == null)
			throw new Exception("Class didn't contain a plugin!");

		// Python Umgebung und Klassen laden
		py = new PythonInterpreter();
		py.execfile(pluginFiles + "/" + filePath);

		// Object erstellen
		String objName = "plugin" + className + System.currentTimeMillis();
		py.exec(objName + " = " + className + "()");
		pyPlugin = py.get(objName);
	}

	public void addParameter(String parameter) {
		pyPlugin.invoke("addParameter", new PyString(parameter));
	}

	public void addResource(String name, Object res) {
		pyPlugin.invoke("addResource", new PyString(name), 
				new PyJavaInstance(res));
	}

	public String execute() {
		return pyPlugin.invoke("execute").toString();
	}

	public String getDiscription() {
		return pyPlugin.invoke("getDiscription").toString();
	}
}
