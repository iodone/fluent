#!/usr/bin/env bash
THIS_DIR=`dirname $(readlink -f $0)`
project_name=$1
harbor_url=$2
tag=$3

docker pull ${harbor_url}:${tag} > /dev/null 2>&1
if [[ $? == 0 ]];then
    echo image ${project_name}:${tag} exits
else
	mv ./output/${project_name}*.tgz ./output/${project_name}.tgz
	tar -xzf ./output/${project_name}.tgz -C ./output
	mv ./output/${project_name}* ./output/${project_name}
	docker build --rm -t ${harbor_url}:${tag} ./output/${project_name} > /dev/null 2>&1
	if [[ $? == 0 ]]; then
		echo successfully build image of ${project_name}:${tag}
	else
		echo failed to build image of ${project_name}:${tag}
		exit 1
	fi
    docker push ${harbor_url}:${tag}
	docker rmi -f ${harbor_url}:${tag}
fi
