//package com.eairlv.gateway.zuul.event;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
//import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TimerTest {
//
//    @Autowired
//    ApplicationEventPublisher publisher;
//
//    @Autowired
//    RouteLocator routeLocator;
//
//    public void refresh(){
//        publisher.publishEvent(new RoutesRefreshedEvent(routeLocator));
//    }
//
//    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
//    public void load(){
////        refresh();
//    }
//}
