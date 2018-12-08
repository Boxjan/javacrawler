# README

本项目使用 [maven](https://maven.apache.org/) 进行构建, 使用时请先使用 mvn clean package 进行构建, 接着使用 mvn exec:java 运行


本项目使用到的内容:
- SQLite: 轻量级数据库, 效率较低, 但对环境没有较大要求.
- HttpComponents: 用以发起http连接
- Gson: Google 提供的用于解析Json的库
- Anima:[Link](https://github.com/biezhi/anima) Github上开源的数据库操作库，可操作多种类型数据库

演示使用 SQLite，但由于sqlite 处理效率低，故为单线程处理结果。
若使用其他数据库，可更改 src/java/thread/ProcessThread.java 以获得更高性能

数据库配置位于 config.properties 内 
remote.properties 中包含 Ua 以及代理设置， 代理请按照
```json
"{"type": "[socks|http]", "address": [ip], "port": [port], "username":"[username]"} "password":"[password]"}
```
的方式填入。


爬虫的处理类需要继承 Crawler 类，并通过addClient方法向任务池中加入任务，这样能通过反射调用来源中的Process进行处理

数据库操作位于 Model 中，请根据需要进行重写， 原有代码根据使用的 Anima 编写，请根据需要取舍

