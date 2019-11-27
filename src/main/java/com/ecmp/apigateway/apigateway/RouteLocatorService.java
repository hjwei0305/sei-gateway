package com.ecmp.apigateway.apigateway;

import com.ecmp.apigateway.manager.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.manager.entity.GatewayApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

@Component
public class RouteLocatorService implements RouteDefinitionRepository {

    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;

    @Autowired
    private Environment env;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        List<GatewayApiService> results = gatewayApiServiceDao.findByDeletedFalseAndServiceAppEnabledTrue();
        if (!results.isEmpty()) {
            String contextPath = env.getProperty("server.servlet.context-path","/api-gateway");
            results.forEach(result -> {
                if (StringUtils.isNotBlank(result.getServicePath())
                        && StringUtils.isNotBlank(result.getServiceAppUrl())) {
                    RouteDefinition routeDefinition = new RouteDefinition();
                    //定义第一个断言
                    PredicateDefinition predicate = new PredicateDefinition();
                    predicate.setName("Path");

                    Map<String, String> predicateParams = new HashMap<>(8);
                    predicateParams.put("pattern", contextPath+result.getServicePath());
                    predicate.setArgs(predicateParams);

                    URI uri = URI.create(result.getServiceAppUrl());
                    routeDefinition.setPredicates(Collections.singletonList(predicate));

                    FilterDefinition filter = new FilterDefinition();
                    filter.addArg("_genkey_0","1");
                    filter.setName("StripPrefix");
                    routeDefinition.setFilters(Collections.singletonList(filter));
                    routeDefinition.setUri(uri);
                    routeDefinition.setId(result.getId());
                    routeDefinitions.add(routeDefinition);
                }
            });
        }
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}