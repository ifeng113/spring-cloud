#spring cloud zuul
##跨域配置
```
@Bean
public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", buildConfig());
    return new CorsFilter(source);
}

private CorsConfiguration buildConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    // 1允许任何域名使用
    corsConfiguration.addAllowedOrigin("*");
    // 2允许任何头
    corsConfiguration.addAllowedHeader("*");
    // 3允许任何方法（post、get等）
    corsConfiguration.addAllowedMethod("*");
    return corsConfiguration;
}
```
##理由配置
>静态配置
```
zuul:
  routes:
    beego:
      path: /beego/**
      url: http://localhost:8080
    webgo:
      path: /webgo/**
      url: http://localhost:9090
```
>动态配置
```
public abstract class AbstractDynamicRouter extends SimpleRouteLocator implements RefreshableRouteLocator{
    
    @Override
    public void refresh() {
        doRefresh();
    }
    
    ......

    protected abstract List<BasicRoute> readRoutes();
}

@Bean
DynamicRouter dynamicRouter(){
    return new DynamicRouter(null, this.zuulProperties);
}

#触发刷新

public void refresh(){
    publisher.publishEvent(new RoutesRefreshedEvent(routeLocator));
}
```
>动态配置(spring cloud config方式)
```
#默认加载
<spring.cloud.config.label>/<spring.application.name>-<spring.cloud.config.profile>.yml

#开启web接口，方便触发更新与查看结果
management:
  endpoints:
    web:
      exposure:
        include: refresh,routes
        
方式一：
直接在启动类上加入@RefreshScope，zuul路由刷新，但未删除已修改之前的路由

方式二：
@Configuration
public class ZuulConfig {

    @Bean(name="zuul.CONFIGURATION_PROPERTIES") # 默认，可不加
    @RefreshScope
    @ConfigurationProperties("zuul") # 默认，可不加
    @Primary # 不加优先注入，启动报错
    public ZuulProperties zuulProperties() {
        return new ZuulProperties();
    }
}
zuul路由刷新，覆盖更新

```

#spring cloud eureka client
>高可用配置
```
spring:
  application:
    name: eureka-client
  cloud:
    config:
      profile: dev
      label: master
      discovery:
        enabled: true
        service-id: config-server
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka/ # eureka-server地址，多个用逗号隔开
  instance:
    prefer-ip-address: true # 微服务IP交互（默认为主机名）
    ip-address: 127.0.0.1 # docker宿主机IP
    instance-id: ${eureka.instance.ip-address}:${server.port}:${spring.application.name}
```

#spring cloud config client
>配置需写入bootstrap.yml中，否则端口为默认8888
>>config-server未注册至eureka-server
```
spring:
  application:
    name: zuul
  cloud:
    config:
      label: master
      profile: dev
      uri: http://localhost:10010
 ```
>>config-server注册至eureka-server
 ```
spring:
  application:
    name: zuul
  cloud:
    config:
      label: master
      profile: dev
      discovery:
        enabled: true
        service-id: config-server
```
