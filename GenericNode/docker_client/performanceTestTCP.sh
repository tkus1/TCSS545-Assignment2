echo "Start TCP Test"
echo "Start single thread TCP Test"
time ./bigtest_tc.sh
echo "Start multi thread TDP Test"
time ./parallelTestTCP.sh
