FROM ubuntu:20.04
RUN apt-get update
RUN apt-get install -y default-jre 
RUN apt-get install -y net-tools
RUN apt-get install -y parallel
COPY GenericNode.jar /
COPY entrypoint_client.sh /
COPY bigtest_tc.sh /
RUN chmod +x /bigtest_tc.sh
COPY parallelTest.sh /
RUN chmod +x /parallelTest.sh
ENTRYPOINT ["/entrypoint_client.sh"]
CMD ["300"]