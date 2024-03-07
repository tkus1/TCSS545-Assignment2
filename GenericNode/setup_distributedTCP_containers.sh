#!/bin/bash
# Build java project
./build_project.sh

docker image build -t ubuntu:distTCPServer docker_server/DistributedTCP_server/
docker image build -t ubuntu:TCPClient docker_client/

echo "Stopping running UDP containers..."
docker container rm --force DistTCPServer1
docker container rm --force DistTCPServer2
docker container rm --force DistTCPServer3
docker container rm --force TCPClient

echo "Removing DistributedTCPNetwork network..."
docker network rm DistributedTCPNetwork
echo "Creating DistributedTCPNetwork network..."
docker network create DistributedTCPNetwork
echo "Running server and client container..."
docker container run --name DistTCPServer1 --network=DistributedTCPNetwork --rm -d -p 8080:8080 -p 8081:8081  ubuntu:distTCPServer
docker container run --name DistTCPServer2 --network=DistributedTCPNetwork --rm -d -p 8082:8080 -p 8083:8081  ubuntu:distTCPServer
docker container run --name DistTCPServer3 --network=DistributedTCPNetwork --rm -d -p 8084:8080 -p 8085:8081  ubuntu:distTCPServer
#docker container run --name UDPClient --network=UDPNetwork --rm -d -p 8081:8080 ubuntu:UDPClient
docker container run --name TCPClient --network=DistributedTCPNetwork --rm -d ubuntu:TCPClient