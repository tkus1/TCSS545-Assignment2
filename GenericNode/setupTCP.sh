./buildAndCopy.sh

docker image build -t ubuntu:TCPServer docker_server/
docker image build -t ubuntu:TCPClient docker_client/
echo "Stopping running TCP containers..."
#docker stop TCPServer
#docker stop TCPClient
docker container rm --force TCPServer
docker container rm --force TCPClient

echo "Removing TCP network..."
docker network rm TCPNetwork
echo "Creating TCP network..."
docker network create TCPNetwork
echo "Running server and client container..."
docker container run --name TCPServer --network=TCPNetwork --rm -d -p 8080:1234 ubuntu:TCPServer
docker container run --name TCPClient --network=TCPNetwork --rm -d ubuntu:TCPClient
