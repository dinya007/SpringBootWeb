#!/usr/bin/env bash

#docker-machine start default
#docker-machine regenerate-certs default

docker-machine start consul-node
docker-machine regenerate-certs consul-node
eval "$(docker-machine env consul-node)"
docker run -d \
    -p "8500:8500" \
    -h "consul" \
    progrium/consul -server -bootstrap

docker-machine start web-node
docker-machine regenerate-certs web-node

docker-machine start hello-node
docker-machine regenerate-certs hello-node

docker-machine start world-node
docker-machine regenerate-certs world-node

eval "$(docker-machine env --swarm web-node)"