#!/bin/bash
# Build java project
mvn clean -f pom.xml
mvn verify -f pom.xml
cp target/GenericNode.jar docker_server/
cp target/GenericNode.jar docker_client/