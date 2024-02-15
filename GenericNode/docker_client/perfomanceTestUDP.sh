echo "Start UDP Test"
echo "Start single thread UDP Test"
time ./bigtest_uc.sh
echo "Start multi thread UDP Test"
time ./parallelTestUDP.sh
