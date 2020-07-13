#!/usr/bin/env bash
THIS_DIR=`dirname $(readlink -f $0)`
OUTPUT_DIR=$THIS_DIR/output

mkdir -p $OUTPUT_DIR
rm -rf $OUTPUT_DIR/*

sbt clean
echo sbt build package ...
sbt -Denv=$1 universal:packageZipTarball
cp -f ${THIS_DIR}/target/universal/fluent.tgz $OUTPUT_DIR
echo fluent released ...

