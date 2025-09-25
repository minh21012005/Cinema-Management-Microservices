package com.example.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SwaggerGatewayConfig {

    private final DiscoveryClient discoveryClient;

    public SwaggerGatewayConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    public RouteLocator swaggerRoutes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routesBuilder = builder.routes();

        discoveryClient.getServices().forEach(serviceId -> {
            String serviceLower = serviceId.toLowerCase();
            routesBuilder.route(serviceLower, r -> r
                    .path("/" + serviceLower + "/v3/api-docs/**")
                    .filters(f -> f.rewritePath("/" + serviceLower + "/v3/api-docs/(?<path>.*)", "/v3/api-docs/${path}"))
                    .uri("lb://" + serviceId)
            );
        });

        return routesBuilder.build();
    }

    @Bean
    public List<GroupedOpenApi> apis() {
        List<String> services = discoveryClient.getServices();
        return services.stream()
                .map(serviceId -> GroupedOpenApi.builder()
                        .group(serviceId)
                        .pathsToMatch("/" + serviceId.toLowerCase() + "/**")
                        .build())
                .collect(Collectors.toList());
    }

}
