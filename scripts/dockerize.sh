#!/usr/bin/env bash
DIR=`dirname $(readlink -f $0)`
OLDPWD=`pwd`

cd $DIR/../

activator dist

docker login -u $DOCKER_USERNAME -e $DOCKER_EMAIL -p $DOCKER_PASSWORD
docker build -t thoughtworksturkey/marketwatcher-trends-service .
docker push thoughtworksturkey/marketwatcher-trends-service

cd $OLDPWD
