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

        // Lấy thông tin Authentication từ SecurityContext
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .flatMap(auth -> {
                    if (auth != null && auth.isAuthenticated()) {
                        String email = null;
                        // Lấy email hoặc username từ token

                        if (auth instanceof JwtAuthenticationToken jwtAuth) {
                            email = jwtAuth.getToken().getClaimAsString("email");
                        }

                        if (email == null || email.isEmpty()) {
                            email = auth.getName(); // fallback: lấy sub
                        }
                        // Thêm vào header để gửi tới service MVC
                        String finalEmail = email;
                        ServerWebExchange mutated = exchange.mutate()
                                .request(r -> r.header("X-User-Email", finalEmail))
                                .build();

                        return chain.filter(mutated);
                    } else {
                        // Nếu không có authentication thì forward bình thường
                        return chain.filter(exchange);
                    }
                })
                .switchIfEmpty(chain.filter(exchange)); // nếu SecurityContext rỗng
    }
}
