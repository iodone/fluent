#!/bin/bash
set -e

THIS_DIR=`dirname $(readlink -f $0)`

mkdir -p $THIS_DIR/../var/run
mkdir -p $THIS_DIR/../var/log
mkdir -p $THIS_DIR/../var/tmp

# start
exec "$@"