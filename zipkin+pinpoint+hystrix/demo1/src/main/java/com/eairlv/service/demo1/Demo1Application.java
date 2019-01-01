package com.eairlv.service.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@SpringBootApplication
@EnableFeignClients
//@EnableHystrixDashboard
//@EnableCircuitBreaker
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Demo2Feign demo2Feign;

    @GetMapping("/demo1")
    public String demo1(){
        return "demo1";
    }

    @GetMapping("/demo2")
    public String demo2(){
        return restTemplate.getForObject("http://DEMO2/demo12", String.class);
    }

    @GetMapping("/feign")
    public String feign(){
        return demo2Feign.demo2("abc", "cc");
    }

    @GetMapping("/feign2")
    public String feign2(){
        PObj pObj = new PObj();
        pObj.setUser("lv");
        pObj.setAge(13);
        return demo2Feign.demo2o(pObj);
    }

//    @GetMapping("/demo21")
//    public String demo21(){
//        return new RestTemplate().getForObject("http://localhost:8082/demo12", String.class);
//    }
}

