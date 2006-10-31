import java 
from command import *

class Ip(Command):
    def getDiscription(self):
        return "This Command returns client IP-Addr as string (eg. 192.168.0.32)"
    
    def execute(self):
        sess = self.res['session']
        return sess.getIp()