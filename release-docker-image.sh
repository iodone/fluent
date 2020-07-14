#!/usr/bin/env bash
THIS_DIR=`dirname $(readlink -f $0)`

sbt clean
echo sbt build image ...
sbt -Denv=${env} docker
echo image released ...
