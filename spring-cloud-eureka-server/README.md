#spring cloud eureka server
>基础配置
```
spring:
  application:
    name: eureka-server
server:
  port: 10001
eureka:
  instance:
    hostname: 127.0.0.1
  client:
    fetch-registry: false
    service-url:
      defaultZone: http://127.0.0.1:10002/eureka/
```
```
spring:
  application:
    name: eureka-server
server:
  port: 10002
eureka:
  instance:
    hostname: 127.0.0.1
  client:
    fetch-registry: false
    service-url:
      defaultZone: http://127.0.0.1:10001/eureka/
```
>高可用配置
```
spring:
  application:
    name: eureka-server
  resources:
    static-locations: classpath:/resources/,classpath:/static/ # 主动下线配置
server:
  port: 8761
eureka:
  instance:
    hostname: localhost
  client:
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
    healthcheck:
      enabled: true
  server:
    eviction-interval-timer-in-ms: 90000
    enableSelfPreservation: false
```
>被动下线
>>由client端主动告诉server端自身租期到期时间，在到期时间内租期未更新，则被server端标记未失效，server端每隔一个清理间隔便会清理掉所有的失效服务
```
server端：
    eureka.server.enable-self-preservation=false//（设为false，关闭自我保护）
    eureka.server.eviction-interval-timer-in-ms=9000//清理间隔（单位毫秒，默认是60*1000）
client端（提供方）：
    eureka.instance.lease-renewal-interval-in-seconds=5//租期更新时间间隔（默认30秒）
    eureka.instance.lease-expiration-duration-in-seconds=15//租期到期时间（默认90秒）
client端（调用方）：
    eureka.client.registry-fetch-interval-seconds=5//刷新eureka本地缓存时间间隔
    ribbon.ServerListRefreshInterval=5000//刷新server列表的时间间隔
```
>>>ribbon配置项
```
<clientName>.ribbon.MaxAutoRetries=1
<clientName>.ribbon.MaxAutoRetriesNextServer=1
<clientName>.ribbon.OkToRetryOnAllOperations=true
<clientName>.ribbon.ServerListRefreshInterval=2000
<clientName>.ribbon.ConnectTimeout=3000
<clientName>.ribbon.ReadTimeout=3000
<clientName>.ribbon.listOfServers=127.0.0.1:8080,127.0.0.1:8085
<clientName>.ribbon.EnablePrimeConnections=false

<clientName>.<nameSpace>.<propertyName>=<value>
<clientName>：这是调用ribbon的客户端名称，如果此值为没有配置，则此条属性会作用到所有的客户端
<nameSpace>：默认值为 "ribbon"
<propertyName>：所有的可用的属性都在com.netflix.client.conf.CommonClientConfigKey

MaxAutoRetries（重试实例重试次数） +  MaxAutoRetriesNextServer*(1（重试实例尝试次数） + MaxAutoRetries（重试实例重试次数）) 其中MaxAutoRetriesNextServer默认为1
```

>主动下线
>>问题：服务集群关闭会出现失效的情况  
>>原因：服务一旦被调用或者保持心态则会被重新激活
```
public static final String HTTP = "http://";
public static final String LOCALHOST = "localhost:";
public static final String EUREKA_APPS = "/eureka/apps/";
public static final String DASH = "/";
public static final String COMMA = ":";

@Value("${server.port}")
private Integer port;

@Autowired
private RestTemplate restTemplate;

@DeleteMapping("/{application}/instance/{instanceId:.+}") # path类型的url传递的字符串中如果带'.'需特殊处理
public Result deleteAppInstance(@PathVariable("application") String application,
                                @PathVariable("instanceId") String instanceId) {
    String[] str = instanceId.split(COMMA);
    String shutdownUrl = HTTP + str[0] + COMMA + str[1] + DASH + "shutdown";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = null;
    try {
        response = restTemplate.postForEntity(shutdownUrl, requestEntity, String.class);
    } catch (Exception e) {
        log.warn("catch exception {}", e.toString());
    }

    String instanceUrl = HTTP + LOCALHOST + port + EUREKA_APPS + application + DASH + instanceId;
    restTemplate.delete(instanceUrl);
    if (response != null && response.getStatusCode() == HttpStatus.OK) {
        return Result.success("移除微服务实例" + application + COMMA + instanceId + "成功");
    }

    return Result.fail("移除微服务实例" + application + COMMA + instanceId + "失败");
}
```
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Eureka Console</title>
</head>
<script src="jquery-3.3.1.js"></script>
<style>
.remove{
    color: red;
    cursor: pointer;
}
.remove:hover{
    color: red;
}

</style>
<script>
    $.get("/", function(page){
        $("body").html(page);

        $("#instances tbody tr").each(function(){
            var _html = "<b>&nbsp;（<a class='remove'>shutdown</a>）</b>";
            $(this).find("td").eq(3).find("a").each(function () {
                $(this).after(_html);
            })
        })

        $(".remove").click(function () {
            var tr = $(this).parent().parent().parent();
            var application = tr.find("td").eq(0).find("b").html();
            var instance = $(this).parent().prevAll().html();
            $.ajax({
                url: "/management/apps/" + application + "/instance/" + instance,
                type: "DELETE",
                success: function(){
                    window.location.reload();
                }
            })
        });
    })
</script>
<body>

</body>
</html>
```
>降级与熔断
```
由于网络波动，微服务之间的调用出现异常时，hystrix熔断器会将服务提供者降级
```
>>ribbon  
```
负载均衡器
使用restTemplate+Ribbon可完成为服务间的调用
@Bean
@LoadBalanced
RestTemplate restTemplate(){
    return new RestTemplate();
}
```
>>feign 
```
基于ribbon负载均衡的微服务调用客户端，默认依赖hystrix
@FeignClient(value = "adas-server-base-driver", fallback = BaseDriverHandler.class)
``` 
>>hystrix
```
熔断器（可参考：https://www.jianshu.com/p/138f92aa83dc）
降级：服务调用异常时的处理，即执行fallback中的方法
熔断：当hystrix出现一定频次的降级（调用的目标服务不可用）之后，服务调用者自身熔断，不再访问目标服务，直接降级处理

#开启熔断
feign:
  hystrix:
    enabled: true

#调用线程允许请求HystrixCommand.GetFallback()(Feign降级时)的最大数量
hystrix.command.default.fallback.isolation.semaphore.maxConcurrentRequests

#线程池核心线程数
hystrix.threadpool.default.coreSize

```
>>>hystrix信号隔离
```
hystrix:
  command:
    default:
      execution:
        isolation:
          strategy: SEMAPHORE
```
>>>hystrix线程隔离
```
ribbon:
  ReadTimeout: 2000
  ConnectTimeout: 1000
  MaxAutoRetries: 1
  MaxAutoRetriesNextServer: 1

hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 12000
```
#杂项
>eureka server端与client端需要@Enable...注解  
>config server端需要@Enable...注解，client端不需要