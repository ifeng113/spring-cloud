package com.eairlv.gateway.gateway.event;

import com.eairlv.gateway.gateway.service.GatewayApiRouteService;
import com.eairlv.gateway.gateway.service.DynamicRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimerTest {

    @Autowired
    DynamicRouteDefinitionRepository definitionRepository;

    @Autowired
    GatewayApiRouteService gatewayApiRouteService;

    private boolean add = false;

    private boolean delete = false;

    private boolean timer = false;

//    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void api(){
        System.out.println(System.currentTimeMillis());
        if (!add){
            gatewayApiRouteService.add();
            add = !add;
        }else if (!delete){
            gatewayApiRouteService.delete();
            delete = !delete;
        }
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void dynamic(){
        System.out.println(System.currentTimeMillis());
        if (!timer){
            definitionRepository.refresh();
            gatewayApiRouteService.refresh();
            timer = !timer;
        }
    }

}
