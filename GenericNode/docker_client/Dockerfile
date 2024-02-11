FROM ubuntu:20.04
RUN apt-get update
RUN apt-get install -y default-jre 
RUN apt-get install -y net-tools
RUN apt-get install -y parallel
COPY GenericNode.jar /
COPY entrypoint_client.sh /

COPY bigtest_tc.sh /
RUN chmod +x /bigtest_tc.sh
COPY parallelTestTCP.sh /
RUN chmod +x /parallelTestTCP.sh

COPY bigtest_uc.sh /
RUN chmod +x /bigtest_uc.sh
COPY parallelTestUDP.sh /
RUN chmod +x /parallelTestUDP.sh

ENTRYPOINT ["/entrypoint_client.sh"]
CMD ["300"]