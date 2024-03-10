#!/bin/bash
# Build java project
./build_project.sh

# Build docker images for server and client
docker image build -t ubuntu:distTCPServer docker_server/DistributedTCP_server/
docker image build -t ubuntu:TCPClient docker_client/
docker image build -t haproxy:distTCPHaproxy docker_server/haproxy/

echo "Stopping running TCP containers..."
docker container rm --force DistTCPServer1
docker container rm --force DistTCPServer2
docker container rm --force DistTCPServer3
docker container rm --force TCPClient

echo "Stopping running Haproxy container..."
docker container rm --force Haproxy

echo "Removing DistributedTCPNetwork network..."
docker network rm DistributedTCPNetwork
echo "Creating DistributedTCPNetwork network..."
docker network create DistributedTCPNetwork
echo "Running server and client container..."
docker container run --name DistTCPServer1 --network=DistributedTCPNetwork --rm -d -p 8081:8080 -p 8082:8081  ubuntu:distTCPServer
docker container run --name DistTCPServer2 --network=DistributedTCPNetwork --rm -d -p 8083:8080 -p 8084:8081  ubuntu:distTCPServer
docker container run --name DistTCPServer3 --network=DistributedTCPNetwork --rm -d -p 8085:8080 -p 8086:8081  ubuntu:distTCPServer

echo "Running Haproxy container..."
docker container run --name Haproxy --network=DistributedTCPNetwork --rm -d -p 8080:80 haproxy:distTCPHaproxy
#docker container run --name UDPClient --network=UDPNetwork --rm -d -p 8081:8080 ubuntu:UDPClient
docker container run --name TCPClient --network=DistributedTCPNetwork --rm -d ubuntu:TCPClient