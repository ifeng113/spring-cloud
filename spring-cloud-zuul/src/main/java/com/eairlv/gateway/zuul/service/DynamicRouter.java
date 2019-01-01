//package com.eairlv.gateway.zuul.service;
//
//import com.eairlv.gateway.zuul.entity.BasicRoute;
//import com.eairlv.gateway.zuul.entity.Context;
//import com.google.common.collect.Lists;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.stream.Collectors;
//
//public class DynamicRouter extends AbstractDynamicRouter {
//
////    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicRouter.class);
////    public static final String PROPERTIES_FILE = "router.properties";
////    private static final String ZUUL_ROUTER_PREFIX = "zuul.routes";
//
//    public DynamicRouter(String servletPath, ZuulProperties properties) {
//        super(servletPath, properties);
//    }
//
//    @Override
//    protected List<BasicRoute> readRoutes() {
//        List<BasicRoute> list = Lists.newArrayListWithExpectedSize(3);
//        list.add(BasicRoute.builder()
//                .id("beego")
//                .path("/beego/**")
//                .url("http://localhost:8080")
//                .stripPrefix(true)
//                .build());
//        list.add(BasicRoute.builder()
//                .id("webgo")
//                .path("/webgo/**")
//                .url("http://localhost:9090")
//                .stripPrefix(true)
//                .build());
//        return list;
//    }
//
////    @Override
////    protected List<BasicRoute> readRoutes() {
////        List<BasicRoute> list = Lists.newArrayListWithExpectedSize(3);
////        try {
////            Properties prop = new Properties();
////            prop.load(
////                    this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)
////            );
////
////            Context context = new Context(new HashMap<>((Map) prop));
////            Map<String, String> data = context.getSubProperties(ZUUL_ROUTER_PREFIX);
////            List<String> ids = data.keySet().stream().map(s -> s.substring(0, s.indexOf("."))).distinct().collect(Collectors.toList());
////            ids.stream().forEach(id -> {
////                Map<String, String> router = context.getSubProperties(String.join(".", ZUUL_ROUTER_PREFIX, id));
////
////                String path = router.get("path");
////                path = path.startsWith("/") ? path : "/" + path;
////
////                String serviceId = router.getOrDefault("serviceId", null);
////                String url = router.getOrDefault("url", null);
////
////                BasicRoute basicRoute = new BasicRoute();
////                basicRoute.setId(id);
////                basicRoute.setPath(path);
////                basicRoute.setUrl(router.getOrDefault("url", null));
////                basicRoute.setServiceId((StringUtils.isBlank(url) && StringUtils.isBlank(serviceId)) ? id : serviceId);
////                basicRoute.setRetryable(Boolean.parseBoolean(router.getOrDefault("retryable", "false")));
////                basicRoute.setStripPrefix(Boolean.parseBoolean(router.getOrDefault("strip-prefix", "false")));
////                list.add(basicRoute);
////            });
////        } catch (IOException e) {
////            LOGGER.info("error to read " + PROPERTIES_FILE + " :{}", e);
////        }
////        return list;
////    }
//}
