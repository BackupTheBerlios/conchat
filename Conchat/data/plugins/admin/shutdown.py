import java
from command import *

class Ip(Command):
    def getDiscription(self):
        return "This admincommand remove the ip-addr from banlist. Parameter: password, ip-addr"
    
    def execute(self):
        if len(self.param) < 1:
            return "/error wrong parameter count"
        else:
            if self.res['host'].isAdminPassword(self.param[0]):
                self.res['host'].stop()
                return "!<EXIT>!"
            else:
                return "/error not authorized"