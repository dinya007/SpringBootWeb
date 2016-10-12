#!/usr/bin/env bash

#eval "$(docker-machine env default)"

#docker network create --driver overlay --subnet=10.0.9.0/24 web-net
#docker network create --driver overlay --subnet=10.0.9.0/24 hello-net
#docker network create --driver overlay  --subnet=10.0.9.0/24 world-net

eval $(docker-machine env --swarm web-node)

docker network create --driver overlay web-net
docker network create --driver overlay hello-net
docker network create --driver overlay world-net