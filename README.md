# Fluent   
Fluent是基于Akka-HTTP实现的一个HTTP服务框架。为Scala新手提供一个容易快速上手且功能全面的基础服务框架。

## Design Philosophy
遵循 《clean architechture》的宗旨，参见 《try clean architecture on Golang》

## Clean Architecture 
![image](./doc/assets/CleanArchitecture.jpg)
![image](./doc/assets/clean-arch.png)

## Quickstart


## Deploy

### 本地运行
1.编译发布
```aidl
./realase-package.sh [dev|test|prod] // 对应开发、测试和生产环境
```
2.当前目录下会生成output目录，fluent会打包发布到该目录，解压fluent.tgz
```aidl
tar -xzvf fluent.tgz
// 目录结构
fluent
├── bin
│   ├── app.sh
│   └── entry-point.sh
├── config
│   ├── application.conf
│   ├── logback.xml
│   └── supervisor.conf
├── lib
│   └── fluent.jar
└── migrate
    └── db
        └── migration
            ├── U1.0__Create_order_table.sql
            ├── U1.1__Fix_indexes.sql
            ├── V1.0__Create_test_table.sql
            └── V1.1__indexes_insertion.sql
```
3.初始化数据库
```aidl
bin/app.sh migrate
```
4.启动应用
```aidl
bin/app.sh start
bin/app.sh stop // 停止
``` 

### 容器化
1. 打包编译镜像
```aidl
./release-docker-image.sh [dev|test|prod]
```
会将fluent/fluent image 推送到local仓库，`docker images` 查看是否成功

2.运行
修改配置文件(容器里面有一份，可以先copy出来，作为模板), 存放到当前目录的var/config
```shell
## copy config file
docker run -v `pwd`/backup:/backup --rm fluent/fluent cp -r /root/app/fluent/config /backup/
## run docker
docker run --name fluent -p 9002:9002 -v `pwd`/var/config:/root/app/fluent/config  --rm fluent/fluent
```
## Reference

框架具体设计和实现参考：
1. https://github.com/bxcodec/go-clean-arch
2. https://github.com/ArchDev/akka-http-rest
3. https://github.com/mDialog/smoke
4. https://github.com/lihaoyi/cask
5. https://github.com/akka-fusion/akka-fusion
6. https://github.com/iodone/caliban
7. https://github.com/pauljamescleary/scala-pet-store
8. https://github.com/howiehu/ddd-architecture-samples
9. https://github.com/finagle/finch
