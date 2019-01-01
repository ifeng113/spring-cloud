package com.eairlv.gateway.gateway.service;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

import static java.util.Collections.synchronizedMap;

@Component
public class DynamicRouteDefinitionRepository implements RouteDefinitionRepository {

    private final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<String, RouteDefinition>());

    DynamicRouteDefinitionRepository(){
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

        routes.put("beego", definition);
        routes.put("webgo", definition2);
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    public void refresh() {
        routes.remove("beego");

        RouteDefinition definition = new RouteDefinition();
        definition.setId("beego2");
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080").build().toUri();
        definition.setUri(uri);

        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");
        Map<String, String> predicateParams = new HashMap<>(1);
        predicateParams.put("", "/beego2/**");
        predicate.setArgs(predicateParams);

        FilterDefinition filter = new FilterDefinition();
        filter.setName("StripPrefix");
        Map<String, String> filterParams = new HashMap<>(1);
        filterParams.put("_genkey_0", "1");
        filter.setArgs(filterParams);

        definition.setPredicates(Collections.singletonList(predicate));
        definition.setFilters(Collections.singletonList(filter));


        routes.put("beego2", definition);
    }
}
