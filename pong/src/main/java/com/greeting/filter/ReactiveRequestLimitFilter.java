package com.greeting.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sam
 * @Date 2024-10-28
 */


@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestLimitFilter implements WebFilter {


    private final Logger log = LoggerFactory.getLogger(ReactiveRequestLimitFilter.class);


    public static final String ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!";
    private final ConcurrentHashMap<Long, Long> requestCounts = new ConcurrentHashMap<>();


    @Value("${app.rate.duration:#{1000}}")
    private long rateDuration;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final long currentTime = System.currentTimeMillis();

        return Mono.fromCallable(() -> {
                    Long currentSecondTime = currentTime / 1000;
                    cleanUpRequestCounts(currentSecondTime);

                    Long currentRequest = requestCounts.putIfAbsent(currentSecondTime,1L);
                    if (currentRequest == null) {
                        String errorMessage = String.format(ERROR_MESSAGE, request.getURI().toString(), request.getRemoteAddress(), rateDuration);
                        log.error(errorMessage);
                        throw new RateLimitException(errorMessage);
                    }
                    return Void.TYPE; // Indicate successful execution
                })
                .flatMap(ignore -> chain.filter(exchange)); // Proceed if rate limit is not exceeded
    }

    private void cleanUpRequestCounts(final long currentSecondTime) {
        requestCounts.entrySet().removeIf( entry -> entry.getKey() < currentSecondTime);
    }
}
