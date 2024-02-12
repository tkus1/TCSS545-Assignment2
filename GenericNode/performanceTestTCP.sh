echo "start performance test for TCP server"
echo "start single thread test"
time docker exec -it TCPClient /bin/bash -c /bigtest_tc.sh
echo "start parallel test"
time docker exec -it TCPClient /bin/bash -c /parallelTest.sh
