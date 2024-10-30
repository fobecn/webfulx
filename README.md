# Code Challenge

## Prerequisites

Java 17, spring 3.3.4, maven 3.9,
windows only (Depends on the C drive file path)

## Quick Start
```
mvn install

# start pong server ,  pong must run on 8081, defult 8081
java -Xmx256m -Xms256m -jar pong-0.0.1-SNAPSHOT.jar

# start ping 1 server

java -Xmx256m -Xms256m  -jar  ping-0.0.1-SNAPSHOT.jar --server.port=8082

# start ping 2 server
java -Xmx256m -Xms256m  -jar  ping-0.0.1-SNAPSHOT.jar --server.port=8083

```
## 测试结果
内部网络应该是无法下载相关包，包太新了，我把测试关键点截图也放上来了

./result


## 待优化
1. 代码片段未最优化
2. 未深入理解操作性文件锁
3. spring 停机需要保证pingFileLock正确结束。
4. 不标准的groovy, 本人比较熟悉标准junit.
5. maven 依赖版本未使用参数配置
6. 函数模式编码jacoco无发覆盖！