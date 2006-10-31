#!/bin/bash

vm=`which java`

port=8080
config=data/mapping.xml
read -p "Server-Admin-Password: " password
$vm -cp lib/chatserver.jar:lib/jython.jar:data/plugins com.telekom.bs.chatserver.server.Application $port $config $password

#$vm -cp lib/chatserver.jar:lib/jython.jar:data/plugins com.telekom.bs.chatserver.server.Application $@