#!/bin/bash

echo "Building Java project..."
./build_project.sh

echo "Building Docker images..."
docker build -t rmi-server docker_server/
docker build -t rmi-client docker_client/

echo "Removing existing container..."
docker container rm --force rmi-server
docker container rm --force rmi-client

echo "Running Docker container..."
docker container run --name RMI_server --rm -d rmi-server
docker container run --name RMI_client --rm -d rmi-client