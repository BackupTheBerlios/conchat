import java 
from command import *

class Echo(Command):
    def getDiscription(self):
        return "This Command returns the text you set as parameter"
    
    def execute(self):
        if len(self.param):
            return self.param[0]
        else:
            return "<NO TEXT>"