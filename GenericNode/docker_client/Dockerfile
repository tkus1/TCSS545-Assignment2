FROM ubuntu:20.04
RUN apt-get update
RUN apt-get install -y default-jre 
RUN apt-get install -y net-tools
RUN apt-get install -y parallel
COPY GenericNode.jar /
COPY entrypoint_client.sh /

COPY bigtest_tc.sh /
COPY parallelTestTCP.sh /
COPY performanceTestTCP.sh /

COPY bigtest_uc.sh /
COPY parallelTestUDP.sh /
COPY performanceTestUDP.sh /

COPY bigtest_rc.sh /
COPY parallelTestRMI.sh /
COPY performanceTestRMI.sh /

ENTRYPOINT ["/entrypoint_client.sh"]
CMD ["300"]


