# GenericNode

## Class Overview
### GenericNode
- This is the main class for the project.
- It is the entry point for the project.
- It is responsible for creating the server and client objects and running them.
### SharedResource
- This class provides a shared key value store for the server.
- All threads in the server can access the shared resource.
- Processing should be done in a thread safe manner (synchronize).
### ExtendedEntry
- This class is an extension of the SimpleEntry class.
- In addition to the SimpleEntry class, it also contains a type of method (ex. GET, POST, DELETE, STORE).
- ExtendedEntry is used to store the request data, being sent from the client to the server.
## Requirement
Run on JDK 11.
## Usage
### TCP Sever Test
1. **Run setupTCP.sh for:**
   - Build the project
   - Build the docker image
   - Run the following docker container
     - TCP server will be running on port 8080:1234 
     - TCP client will be running
2. **Run performTCP.sh:**
   - First test is for single thread server and client
   - Second test is for multi thread server and client
### UDP Sever Test
1. **Run setupUDP.sh for:**
   - Build the project
   - Build the docker image
   - Run the following docker container
     - UDP server will be running on port 8080:1234 
     - UDP client will be running
2. **Run performUDP.sh:**
   - First test is for single thread server and client
   - Parallel test cannot be done for UDP as it use the same receive port

   
## Features

## Reference

## Author

[twitter](https://twitter.com/Kotabrog)

