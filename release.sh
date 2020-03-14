#!/usr/bin/env bash
THIS_DIR=`dirname $(readlink -f $0)`
OUTPUT_DIR=${THIS_DIR}/output

project_name=$1
env=$2

mkdir -p ${OUTPUT_DIR}
rm -rf ${OUTPUT_DIR}/*

sbt clean

echo sbt build package ...
sbt -Denv=${env} universal:packageZipTarball
cp -f ${THIS_DIR}/target/universal/${project_name}.tgz ${OUTPUT_DIR}

echo ${project_name}.tgz released
