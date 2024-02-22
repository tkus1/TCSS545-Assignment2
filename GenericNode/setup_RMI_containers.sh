#!/bin/bash

echo "Building Java project..."
./build_project.sh

echo "Building Docker images..."
docker build -t ubuntu:RMIserver docker_server/RMI_server/
docker build -t ubuntu:RMIclient docker_client/

echo "Removing existing container..."
docker container rm --force RMIserver
docker container rm --force RMIclient

echo "Removing existing network..."
docker network rm RMINetwork
echo "Creating RMI network..."
docker network create RMINetwork
echo "Running Docker container..."
docker container run --name RMIserver --network=RMINetwork --rm -d ubuntu:RMIserver
docker container run --name RMIclient --network=RMINetwork --rm -d ubuntu:RMIclient