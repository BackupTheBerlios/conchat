#!/usr/bin/python

import sys

classtext = """import com.telekom.bs.chatserver.server.Command;

public class %s extends Command {

	public void addParameter(String parameter) {}

	public String execute() {
		return "";
	}

	public String getDiscription() {
		return "--- No information. ---";
	}
}
"""

if sys.argv[1] == "--help":
	print " ---- Chatserver Plugincreator ---- "
	print "  Create a new plugin with name:"
	print "  # ", sys.argv[0], " create <Classname>";
	print "  Display help:"
	print "  # ", sys.argv[0], " --help"
elif sys.argv[1] == "create":
	classname = sys.argv[2]
	classfile = file(classname + ".java", "w")
	classfile.write(classtext%classname)
	classfile.close()
