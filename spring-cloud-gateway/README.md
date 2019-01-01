#spring cloud gateway
##跨域配置：
```
private static final String ALL = "*";
private static final String MAX_AGE = "18000L";

@Bean
public WebFilter corsFilter() {
    return (ServerWebExchange ctx, WebFilterChain chain) -> {
        ServerHttpRequest request = ctx.getRequest();
        if (!CorsUtils.isCorsRequest(request)) {
            return chain.filter(ctx);
        }
        HttpHeaders requestHeaders = request.getHeaders();
        ServerHttpResponse response = ctx.getResponse();
        HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
        HttpHeaders headers = response.getHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
        headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
        if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return chain.filter(ctx);
    };
}
```
##路由配置：
###静态配置
> yml：
```
spring:
  cloud:
    gateway:
      routes:
      - id: beego
        uri: http://localhost:8080
        predicates:
        - Path=/beego/**
        filters:
        - StripPrefix=1
      - id: webgo
        uri: http://localhost:9090
        predicates:
        - Path=/webgo/**
        filters:
        - StripPrefix=1
```
> bean注入：
```
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
  StripPrefixGatewayFilterFactory.Config config = new StripPrefixGatewayFilterFactory.Config();
  config.setParts(1);
  return builder.routes()
          .route("beego", r -> r.path("/beego/**").filters(f -> f.stripPrefix(1)).uri("http://localhost:8080"))
          .route("webgo", r -> r.path("/webgo/**").filters(f -> f.stripPrefix(1)).uri("http://localhost:9090"))
          .build();
} 
```
###动态加载
####方式一：动态刷新RouteDefinitionRepository中的routes
```
public class DynamicRouteDefinitionRepository implements RouteDefinitionRepository
```
```  
private final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<String, RouteDefinition>());
```
####方式二：调用RouteDefinitionWriter的save/delete方法
```
routeDefinitionWriter.save(Mono.just(definition)).subscribe();
routeDefinitionWriter.delete(Mono.just(id)).subscribe();
```
####公共：
```
public void refresh() {
    this.publisher.publishEvent(new RefreshRoutesEvent(this));
}
```
