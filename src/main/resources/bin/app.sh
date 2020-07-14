#! /bin/bash

THIS_DIR=`dirname $(readlink -f $0)`
CONF_DIR=$THIS_DIR/../config
MIGRATION_DIR=$THIS_DIR/../migrate
mkdir -p $THIS_DIR/../var/run
mkdir -p $THIS_DIR/../var/log
mkdir -p $THIS_DIR/../tmp


start(){
    cd $THIS_DIR/../
    jar=`ls $THIS_DIR/../lib/`
    nohup java -cp $THIS_DIR/../lib/$jar:$CONF_DIR app.Demo >> /dev/null 2>&1 &
    echo $! > $THIS_DIR/../var/run/pid
    echo "Starting service ... "
    cd -
}

stop(){
    pid=`cat $THIS_DIR/../var/run/pid`
    kill -9 $pid
    echo "" > $THIS_DIR/../var/run/pid
    echo "Stopping service ..."
}

restart(){
    stop
    sleep 3
    start
}

migrate() {
    cd $THIS_DIR/../
    jar=`ls $THIS_DIR/../lib/`
    echo "Migrating database ... "
    java -cp $THIS_DIR/../lib/$jar:$MIGRATION_DIR:$CONF_DIR -Duser.timezone=Asia/Shanghai app.DbMigration
    cd -
}
if [ "$1" = "start" ]
then
    start
elif [ "$1" = "stop" ]
then
    stop
elif [ "$1" = "restart" ]
then
    restart
elif [ "$1" = "migrate" ]
then
    migrate
else
    echo "Useage: app.sh start | stop | restart | migrate"
fi
