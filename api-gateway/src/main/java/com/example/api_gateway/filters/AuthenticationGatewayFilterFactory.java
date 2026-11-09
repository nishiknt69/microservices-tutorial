package com.example.api_gateway.filters;

import com.example.api_gateway.service.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.info("AUTHENTICATION FILTER APPLY CALLED");

        return (exchange, chain) -> {

            log.info("========== LOGGING ORDER FILTER EXECUTED ==========");

            log.info("isEnabled value: {}", config.isEnabled());

            String authorizationHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("Authorization");

            if (authorizationHeader == null ||
                    !authorizationHeader.startsWith("Bearer ")) {

                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            try {
                String token = authorizationHeader.substring(7).trim();

                Long userId = jwtService.getUserIdFromToken(token);

                log.info("JWT verified successfully. User ID: {}", userId);

                ServerHttpRequest mutatedRequest = exchange.getRequest()
                        .mutate()
                        .header("X-User-Id", userId.toString())
                        .build();

                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(mutatedRequest)
                        .build();

                return chain.filter(mutatedExchange);

            } catch (Exception e) {

                log.error("JWT authentication failed: {}", e.getMessage(), e);

                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }
        };
    }


    @Data
    public static class Config{
        private boolean isEnabled;
    }
}
