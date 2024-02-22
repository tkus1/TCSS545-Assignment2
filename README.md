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
