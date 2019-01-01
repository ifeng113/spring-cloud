package com.eairlv.gateway.zuul;

//import com.eairlv.gateway.zuul.service.DynamicRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableZuulProxy
@SpringBootApplication
//@RefreshScope
public class ZuulApplication {

//    @Autowired
//    ZuulProperties zuulProperties;

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

//    @Bean
//    DynamicRouter dynamicRouter(){
//        return new DynamicRouter(null, this.zuulProperties);
//    }
}

