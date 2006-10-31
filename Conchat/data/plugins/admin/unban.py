import java
from command import *

class Ip(Command):
    def getDiscription(self):
        return "This admincommand remove the ip-addr from banlist. Parameter: password, ip-addr"
    
    def execute(self):
        if len(self.param) < 2:
            return "/error wrong parameter count"
        else:
            if self.res['host'].isAdminPassword(self.param[0]):
                if self.res['host'].unban(self.param[1]):
                    return "/successfully removed from banlist"
                else:
                    return "/error user unknown"
            else:
                return "/error not authorized"