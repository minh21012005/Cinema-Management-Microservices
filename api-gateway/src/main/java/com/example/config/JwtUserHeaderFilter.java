package com.example.config;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtUserHeaderFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(ctx -> {
                    String email = null;
                    if (ctx.getAuthentication() instanceof JwtAuthenticationToken jwtAuth) {
                        email = jwtAuth.getToken().getClaimAsString("email");
                    }

                    if (email == null || email.isEmpty()) {
                        email = ctx.getAuthentication() != null ? ctx.getAuthentication().getName() : "";
                    }

                    String finalEmail = email;
                    ServerWebExchange mutated = exchange.mutate()
                            .request(r -> r.header("X-User-Email", finalEmail))
                            .build();

                    return chain.filter(mutated);
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
