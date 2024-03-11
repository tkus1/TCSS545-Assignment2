# Assignment3
## Performance Tests
Performance test can be executed by running GenericNode/docker_client/performanceTestDistTCP.sh
### Set Number of Servers
Number of servers can be set by editing three files below;
- GenericNode/docker_server/DistributedTCP_server/serverlist.cfg (only for the config file tracking method)
- GenericNode/haproxy/haproxy.cfg
- GenericNode/setup_distributedTCP_containers.sh
### Set Node Membership Tracking Methods
You can change node membership tracking methods by modifying GenericNode/GenericNode.java.
![image](https://github.com/tkus1/TCSS545-Assignment2/assets/146398096/7c73292f-922a-44af-9cdf-5e89caab65db)

- TCPServer server = new TCPServer(new ConfigFileGetOtherServersStrategy());
  
  Distributed TCP servers use a config file to track node membership.
- TCPServer server = new TCPServer(new CentralizeMembershipGetOtherServersStrategy());

  Distributed TCP servers track node membership by using a centralized membership key/value store.


# Assignment2

## Overview
It contains scripts mainly for performance testing of UDP, TCP, and RMI clients.

## Project Structure
- `GenericNode/executeAllPerformanceTest.sh`: This script executes performance tests for UDP, TCP, and RMI clients.

## Usage
1. Start the Docker container to call `GenericNode/setupAllContainers.sh`.
2. Execute the `GenericNode/executeAllPerformanceTest.sh` script to do performance tests.

## Performance Tests
- `performanceTestUDP.sh`: Executes performance test for the UDP client.
- `performanceTestTCP.sh`: Executes performance test for the TCP client.
- `performanceTestRMI.sh`: Executes performance test for the RMI client.
- All of above can be done by calling `GenericNode/executeAllPerformanceTest.sh`

Each test measures the time it takes for the client to send a request to the server and for the server to respond. The results are measured using the `time` command and are displayed in three parts: `real` (actual elapsed time), `user` (CPU time used in user mode), and `sys` (CPU time used in system mode).

## Class Overview
- `UDPClient`: This class is responsible for sending and receiving data over UDP.
- `TCPClient`: This class is responsible for sending and receiving data over TCP.
- `RMIClient`: This class is responsible for sending and receiving data over RMI.
