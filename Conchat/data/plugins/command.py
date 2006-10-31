import java

class Command:
    def __init__(self):
        self.param = []
        self.res = {}
    
    def addResource(self, name, res):
        self.res[name] = res
    
    def addParameter(self, parameter):
        self.param.append(parameter)
    
    def getDiscription(self):
        return ""
    
    def execute(self):
        return self.param