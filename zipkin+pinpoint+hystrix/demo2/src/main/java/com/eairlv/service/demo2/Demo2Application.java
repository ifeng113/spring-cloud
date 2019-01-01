package com.eairlv.service.demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@SpringBootApplication
public class Demo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo2Application.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/demo2")
    public String demo2(){
        return "demo2";
    }

//    @GetMapping("/demo1")
//    public String demo1(){
//        return restTemplate.getForObject("http://DEMO1/demo1", String.class);
//    }

    @GetMapping("/demo12")
    public String demo12(){
        return new RestTemplate().getForObject("http://localhost:8081/demo1", String.class);
    }
}

