#!/bin/bash
# Build java project
#mvn clean -f pom.xml
#mvn verify -f pom.xml
#cp target/GenericNode.jar docker_server/
#cp target/GenericNode.jar docker_client/
./buildAndCopy.sh
docker image build -t ubuntu:UDPServer docker_server/
docker image build -t ubuntu:UDPClient docker_client/

echo "Stopping running UDP containers..."
docker container rm --force UDPServer
docker container rm --force UDPClient

echo "Removing UDP network..."
docker network rm UDPNetwork
echo "Creating UDP network..."
docker network create UDPNetwork
echo "Running server and client container..."
docker container run --name UDPServer --network=UDPNetwork --rm -d -p 8080:1234/udp ubuntu:UDPServer
#docker container run --name UDPClient --network=UDPNetwork --rm -d -p 8081:8080 ubuntu:UDPClient
docker container run --name UDPClient --network=UDPNetwork --rm -d ubuntu:UDPClient