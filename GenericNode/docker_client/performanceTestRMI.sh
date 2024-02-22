echo "Start RMI Test"
echo "Start single thread RMI Test"
time ./bigtest_rc.sh
echo "Start multi thread RMI Test"
time ./parallelTestRMI.sh
