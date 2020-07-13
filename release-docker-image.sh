#!/usr/bin/env bash
THIS_DIR=`dirname $(readlink -f $0)`

sbt clean
echo sbt build package ...
sbt -Denv=${env} universal:packageZipTarball
echo ${project_name}.tgz released
