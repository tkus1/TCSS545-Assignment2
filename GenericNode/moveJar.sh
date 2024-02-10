#!/bin/bash
mvn clean -f pom.xml
mvn verify -f pom.xml
cp target/GenericNode.jar docker_server/
docker image build -t ubuntu:TCPServer docker_server/
echo "Stopping all running containers..."
docker stop $(docker ps -q)
docker container run --rm -d -p 8080:1234 ubuntu:TCPServer