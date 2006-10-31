import java
from command import *

class Ip(Command):
    def getDiscription(self):
        return "This admincommand kick the given user. Parameter: password, username"
    
    def execute(self):
        if len(self.param) < 2:
            return "/error wrong parameter count"
        else:
            if self.res['host'].isAdminPassword(self.param[0]):
                if self.res['host'].kick(self.param[1]):
                    return "/successfully kicked"
                else:
                    return "/error user unknown"
            else:
                return "/error not authorized"