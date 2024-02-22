docker exec UDPClient /bin/bash -c ./performanceTestUDP.sh
docker exec TCPClient /bin/bash -c ./performanceTestTCP.sh
docker exec RMIclient /bin/bash -c ./performanceTestRMI.sh