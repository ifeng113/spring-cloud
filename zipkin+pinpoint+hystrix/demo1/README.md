#zipkin
>血案
```
BUG:
@GetMapping("/demo21")
public String demo21(){
    return new RestTemplate().getForObject("http://localhost:8082/demo12", String.class);
}

#通过new RestTemplate()方式的在zipkin中能够查找请求信息，但是在依赖中无法看到调用链，如果要想是调用链，需使用微服务的调用方式：
1.restTemplate + ribbon
@Bean
@LoadBalanced
RestTemplate restTemplate(){
    return new RestTemplate();
}
2.feign
#总之需微服务名，直接的http请求无法显示依赖
```
#pinpoint
>血案  
```
pinpoint-docker-1.8.0使用了mysql，但是mysql的镜像拉去不下来，因此使用pinpoint-docker-1.7.3
```
>难点
```
启动程序时的参数：
java -jar -javaagent:/mnt/docker/pinpoint/env/pinpoint-agent-1.7.3/pinpoint-bootstrap-1.7.3.jar -Dpinpoint.agentId=lv -Dpinpoint.applicationName=lv-app ../env/demo1-0.0.1-SNAPSHOT.jar

访问https://github.com/naver/pinpoint/releases下载和Collector组件相同版本的pinpoint-agent
修改pinpoint-agent中的pinpoint.config，配置项 profiler.collector.ip

# 如果你在多台机器上部署了应用程序，那么就需要在多台机器上部署Agent组件
# ${pinpointPath}是agent组件存放的路径，类似于JAVA_HOME
-javaagent:${pinpointPath}/pinpoint-bootstrap-1.7.3.jar
-Dpinpoint.applicationName=  # 在pinpoint上显示的名字
-Dpinpoint.agentId=          # id,可以和applicationName相同，也可以不同

```
>疑点
```
pinpoint-docker中的pinpoint-agent的作用：
1.关闭docker后无影响，即可关闭
2.查看dockerfile后发现，它的容器是基于alpine:3.7，仅负责获取pinpoint-agent.tar，即只要文件拉取成功，docker容器可关闭
3.dockerfile中配置： VOLUME ["/pinpoint-agent"]  即其他docker容器可以使用 --volumes-from <image-name> 共享目录，达到pinpoint-agent容器化安装，且不直接显式的（-v）挂载至宿主机
```
#feign error handle
>总结
```
# 1.通过@FeignClient注解中的fallbackFactory绑定短路器增强工厂子类
@FeignClient(name = "demo2", fallbackFactory = Demo2FeignFactory.class)

# 2.短路器增强工厂子类继承传递需要代理的Feign接口类
@Component
public class Demo2FeignFactory extends HystrixErrorFactory<Demo2Feign> {}

# 3.实现断路器接口方法，利用反射读取断路器工厂基类泛型参数类型，从而获取Feign接口类，并生成Feign接口类的代理类的实例
public class HystrixErrorFactory<T> implements FallbackFactory<T> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public T create(Throwable throwable) {
        T newProxyInstance = (T) Proxy.newProxyInstance(
                getEntityClass().getClassLoader(),
                new Class[] { getEntityClass() }, new MethodProxy(logger));
        return newProxyInstance;
    }

    private <T> Class<? extends T> getEntityClass() {
        return ReflectionUtil.getSuperClassGenricType(getClass());
    }
}

# 4.动态生成Feign接口类的代理类中未定义接口类中方法的具体实现，仅用作降级才处理，因此无需（也无法）调用 method.invoke(subject, args) 来执行Feign接口类的代理类示例的相应方法，但可以自定义 return 来返回代理类的执行结果
public class MethodProxy implements InvocationHandler {

    private Logger logger;

    public MethodProxy(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return handle(method, args);
    }

    private Object handle(Method method, Object[] args) {
        logger.error("远程调用失败，目标类：" + method.getDeclaringClass());
        logger.error("方法：" + method.getName());
        logger.error("参数：" + new Gson().toJson(args));
        return null;
    }
}

```