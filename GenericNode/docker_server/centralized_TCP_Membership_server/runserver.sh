#!/bin/bash
# Dummy jar file
#java -jar GenericNode.jar

#TCP Server
java -jar GenericNode.jar ts 4410

#UDP Server
#java -jar GenericNode.jar us 1234

#RMI Server
#rmiregistry -J-Djava.class.path=GenericNode.jar &
#java -Djava.rmi.server.codebase=file:GenericNode.jar -cp GenericNode.jar genericnode.GenericNode rmis

