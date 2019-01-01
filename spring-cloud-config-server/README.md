#spring cloud config server
##基础配置
```
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: http://125.71.30.140:18080/lb/config.git # 需要.git结尾
          search-paths: dir # gitlab地址下的目录
          username: lb
          password: lb123123123
      label: master
      profile: dev
server:
  port: 10010
```
##高可用配置
>将config-server注册进eureka-server，并在config-client端使用微服务方式拉取配置