#!/usr/bin/env bash

docker-machine create -d virtualbox consul-node

eval "$(docker-machine env consul-node)"

docker run -d \
    -p "8500:8500" \
    -h "consul" \
    progrium/consul -server -bootstrap

docker-machine create \
    -d virtualbox \
    --swarm --swarm-master \
    --swarm-discovery="consul://$(docker-machine ip consul-node):8500" \
    --engine-opt="cluster-store=consul://$(docker-machine ip consul-node):8500" \
    --engine-opt="cluster-advertise=eth1:2376" \
    web-node

docker-machine create -d \
    virtualbox \
    --swarm \
    --swarm-discovery="consul://$(docker-machine ip consul-node):8500" \
    --engine-opt="cluster-store=consul://$(docker-machine ip consul-node):8500" \
    --engine-opt="cluster-advertise=eth1:2376" \
    hello-node

docker-machine create -d \
    virtualbox \
    --swarm \
    --swarm-discovery="consul://$(docker-machine ip consul-node):8500" \
    --engine-opt="cluster-store=consul://$(docker-machine ip consul-node):8500" \
    --engine-opt="cluster-advertise=eth1:2376" \
    world-node

eval $(docker-machine env --swarm web-node)

docker network create --driver overlay --subnet=10.0.9.0/24 my-net

cd $DOCKER_CERT_PATH
cd ..
cd consul-node
openssl pkcs12 -export \
-inkey key.pem \
-in cert.pem \
-CAfile ca.pem \
-chain \
-name client-side \
-out cert.p12 \
-password pass:password

cd ..
cd web-node
openssl pkcs12 -export \
-inkey key.pem \
-in cert.pem \
-CAfile ca.pem \
-chain \
-name client-side \
-out cert.p12 \
-password pass:password

cd ..
cd hello-node
openssl pkcs12 -export \
-inkey key.pem \
-in cert.pem \
-CAfile ca.pem \
-chain \
-name client-side \
-out cert.p12 \
-password pass:password

cd ..
cd world-node
openssl pkcs12 -export \
-inkey key.pem \
-in cert.pem \
-CAfile ca.pem \
-chain \
-name client-side \
-out cert.p12 \
-password pass:password

#cd ~/Documents/Java/Docker/WebSample/

#docker-compose build
#docker-compose up -d

#curl https://192.168.99.101:2376/containers/json --cert $DOCKER_CERT_PATH/cert.p12 --pass password --key $DOCKER_CERT_PATH/key.pem --cacert $DOCKER_CERT_PATH/ca.pem

#curl https://192.168.99.103:2376/networks/a3c89dc66453 --cert $DOCKER_CERT_PATH/cert.p12 --pass password --key $DOCKER_CERT_PATH/key.pem --cacert $DOCKER_CERT_PATH/ca.pem
#curl https://192.168.99.103:2376/networks/ --cert $DOCKER_CERT_PATH/cert.p12 --pass password --key $DOCKER_CERT_PATH/key.pem --cacert $DOCKER_CERT_PATH/ca.pem

#curl https://192.168.99.103:2376/info/ --cert $DOCKER_CERT_PATH/cert.p12 --pass password --key $DOCKER_CERT_PATH/key.pem --cacert $DOCKER_CERT_PATH/ca.pem

#curl -X GET http://192.168.99.104:8500/v1/agent/members

#curl -X GET http://192.168.99.104:8500/v1/catalog/services

#swarm join --advertise=<node_ip:2375> consul://<consul_addr>/<optional path prefix>

#docker exec -it <name> bash
#exit

#/etc/ssl/certs

curl http://hello:8080/ --cert $DOCKER_CERT_PATH/cert.p12 --pass password --key $DOCKER_CERT_PATH/key.pem --cacert $DOCKER_CERT_PATH/ca.pem
