package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfigCommon {

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @Bean(name = "gatewayOpenAPI")
    public OpenAPI gatewayOpenAPI() {
        String basePath = getBasePath(serviceName);
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080" + basePath).description("API Gateway")
                ))    .info(new Info()
                        .title("Service API")
                        .version("v1")
                        .description("API documentation for Service"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT") // chỉ rõ là JWT
                        )
                );
    }

    private String getBasePath(String serviceName) {
        switch (serviceName) {
            case "auth-service": return "/auth-service";
            case "user-service": return "/user-service";
            case "cinema-service": return "/cinema-service";
            case "movie-service": return "/movie-service";
            case "media-service": return "/media-service";
            default: return "";
        }
    }


}
