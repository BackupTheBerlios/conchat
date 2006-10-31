import java
from command import *

class Ip(Command):
    def getDiscription(self):
        return "This admincommand evals the given command. Parameter: password, command"
    
    def execute(self):
        if len(self.param) < 2:
            return "/error wrong parameter count"
        else:
            if self.res['host'].isAdminPassword(self.param[0]):
                return eval(self.param[1])
            else:
                return "/error not authorized"