#!/bin/bash
# Build java project
mvn clean -f pom.xml
mvn verify -f pom.xml
cp target/GenericNode.jar docker_server/RMI_server/
cp target/GenericNode.jar docker_server/TCP_server/
cp target/GenericNode.jar docker_server/UDP_server/
cp target/GenericNode.jar docker_server/DistributedTCP_server/
cp target/GenericNode.jar docker_server/centralizedTCP_Storage_server
cp target/GenericNode.jar docker_server/centralized_TCP_Membership_server
cp target/GenericNode.jar docker_client/
