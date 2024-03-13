#!/bin/bash
# Build java project
./build_project.sh

# Build docker images for server and client
docker image build -t ubuntu:centralizedTCPStorageServer docker_server/centralizedTCP_Storage_server/
docker image build -t ubuntu:centralizedTCPMembershipServer docker_server/centralized_TCP_Membership_server/
docker image build -t ubuntu:TCPClient docker_client/
# Build haproxy image
docker image build -t haproxy:distTCPHaproxy docker_server/haproxy/

echo "Stopping running TCP containers..."
docker container ls -a | grep DistTCPServer | awk '{print $1}' | xargs docker container rm --force

echo "Stopping running Haproxy container..."
docker container rm --force Haproxy

echo "Removing DistributedTCPNetwork network..."
docker network rm DistributedTCPNetwork
echo "Creating DistributedTCPNetwork network..."
docker network create DistributedTCPNetwork

# Number of servers: 1
#docker-compose -f docker_server/DistributedTCP_server/docker-compose-1-centralized-server.yml up -d

# Number of servers: 3
#docker-compose -f docker_server/DistributedTCP_server/docker-compose-3-centralized-server.yml up -d

# Number of servers: 5
docker-compose -f docker_server/DistributedTCP_server/docker-compose-5-centralized-server.yml up -d

