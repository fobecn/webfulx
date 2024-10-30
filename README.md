# Code Challenge

## Prerequisites
Java 17, spring 3.3.4 maven 3.9
## Quick Start
```
mvn install

# start pong server
java -Xmx256m -Xms256m -jar pong-0.0.1-SNAPSHOT.jar

# start ping 1 server

java -Xmx256m -Xms256m  -jar  ping-0.0.1-SNAPSHOT.jar --server.port=8082

# start ping 2 server
java -Xmx256m -Xms256m  -jar  ping-0.0.1-SNAPSHOT.jar --server.port=8083



```

##
剩余未处理，代码优化
未深入理解操作性文件锁
spring 停机需要保证pingFileLock正确结束。
groovy 测试未能正确编译执行，需要有java文件