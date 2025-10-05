package com.example.config;

import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${minhnb.jwt.base64-secret}")
    private String jwtKey;

    private static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Bean
    @Order(1)
    public SecurityWebFilterChain publicSecurityWebFilterChain(ServerHttpSecurity http) {
        return http
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers(
                        "/auth-service/api/v1/auth/login",
                        "/auth-service/api/v1/auth/register",
                        "/auth-service/api/v1/auth/refresh",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",

                        // Auth service
                        "/auth-service/swagger-ui/**",
                        "/auth-service/v3/api-docs/**",
                        "/auth-service/swagger-resources/**",
                        "/auth-service/webjars/**",

                        // User service
                        "/user-service/swagger-ui/**",
                        "/user-service/v3/api-docs/**",
                        "/user-service/swagger-resources/**",
                        "/user-service/webjars/**",

                        // Movie service
                        "/movie-service/swagger-ui/**",
                        "/movie-service/v3/api-docs/**",
                        "/movie-service/swagger-resources/**",
                        "/movie-service/webjars/**",

                        // Media service
                        "/media-service/swagger-ui/**",
                        "/media-service/v3/api-docs/**",
                        "/media-service/swagger-resources/**",
                        "/media-service/webjars/**",

                        // Cinema service
                        "/cinema-service/swagger-ui/**",
                        "/cinema-service/v3/api-docs/**",
                        "/cinema-service/swagger-resources/**",
                        "/cinema-service/webjars/**",

                        // Booking service
                        "/booking-service/api/v1/sepay/**"
                ))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex.anyExchange().permitAll())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityWebFilterChain securedSecurityWebFilterChain(ServerHttpSecurity http,
                                                                CustomReactiveAuthenticationEntryPoint entryPoint) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        // ✅ Cho phép tất cả OPTIONS request (preflight)
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
                        .authenticationEntryPoint(entryPoint) // đảm bảo token sai cũng gọi đây
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN))
                )
                .addFilterAfter(new JwtUserHeaderFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    // JWT Decoder
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
        return NimbusReactiveJwtDecoder.withSecretKey(key).macAlgorithm(JWT_ALGORITHM).build();
    }

    // CORS WebFilter
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:4173",
                "http://localhost:5173"
                ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "x-no-retry"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
