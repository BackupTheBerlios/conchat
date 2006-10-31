import java 
from command import *

class QuitSession(Command):
    def getDiscription(self):
        return "Disconnect from the Server. The Connection will be closed."
    
    def execute(self):
        sess = self.res['session']
        sess.close()
        return "/userexit"