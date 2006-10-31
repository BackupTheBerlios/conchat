import java
from com.telekom.bs.chatserver.server.config import ChatserverConfig
from com.telekom.bs.chatserver.server.interpreter import CommandMapping
from java.io import File
from command import *

class Ip(Command):
    def getDiscription(self):
        return "This admincommand reload the mappings, to add new commands. Parameter: password, xml-mapping-file"
    
    def execute(self):
        if len(self.param) < 2:
            return "/error wrong parameter count"
        else:
            if self.res['host'].isAdminPassword(self.param[0]):
                file = File(self.param[1])
                if file.exists():
                    config = ChatserverConfig()
                    config.openXml(self.param[1]);
                    CommandMapping.createCommandMapping(config.getCommands(), "data/plugins"); 
                    return "/mapping successfully reloaded"
                else:
                    return "/error no correct path"
            else:
                return "/error not authorized"