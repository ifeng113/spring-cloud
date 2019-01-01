package com.eairlv.gateway.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GatewayApiRouteService {

    @Autowired
    GatewayDynamicRouteService gatewayService;

    public void add(){

        RouteDefinition definition = new RouteDefinition();
        definition.setId("beego");
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080").build().toUri();
        definition.setUri(uri);

        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");
        Map<String, String> predicateParams = new HashMap<>(1);
        predicateParams.put("", "/beego/**");
        predicate.setArgs(predicateParams);

        FilterDefinition filter = new FilterDefinition();
        filter.setName("StripPrefix");
        Map<String, String> filterParams = new HashMap<>(1);
        filterParams.put("_genkey_0", "1");
        filter.setArgs(filterParams);

        definition.setPredicates(Collections.singletonList(predicate));
        definition.setFilters(Collections.singletonList(filter));

        gatewayService.add(definition);

        RouteDefinition definition2 = new RouteDefinition();
        definition2.setId("webgo");
        URI uri2 = UriComponentsBuilder.fromHttpUrl("http://localhost:9090").build().toUri();
        definition2.setUri(uri2);

        PredicateDefinition predicate2 = new PredicateDefinition();
        predicate2.setName("Path");
        Map<String, String> predicateParams2 = new HashMap<>(1);
        predicateParams2.put("", "/webgo/**");
        predicate2.setArgs(predicateParams2);

        FilterDefinition filter2 = new FilterDefinition();
        filter2.setName("StripPrefix");
        Map<String, String> filterParams2 = new HashMap<>(1);
        filterParams2.put("_genkey_0", "1");
        filter2.setArgs(filterParams2);

        definition2.setPredicates(Arrays.asList(predicate2));
        definition2.setFilters(Arrays.asList(filter2));

        gatewayService.add(definition2);
    }

    public void delete(){
        gatewayService.delete("beego");
    }

    public void refresh(){
        gatewayService.refresh();
    }
}
