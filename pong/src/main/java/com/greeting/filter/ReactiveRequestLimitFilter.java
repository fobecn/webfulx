package com.greeting.filter;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestLimitFilter implements WebFilter, InitializingBean {

    public static final String ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!";
    public static final String LIMIT_KEY = "LIMIT_KEY";
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    @Value("${app.rate.limit:#{256}}")
    private int rateLimit;

    @Value("${app.rate.duration:#{1000}}")
    private long rateDuration;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final long currentTime = System.currentTimeMillis();

        return Mono.fromCallable(() -> {
                    cleanUpRequestCounts(currentTime);
                    if (requestCounts.get(LIMIT_KEY).size() > rateLimit) {
                        String errorMessage = String.format(ERROR_MESSAGE, request.getURI().toString(), request.getRemoteAddress(), rateDuration);
                        log.error(errorMessage);
                        throw new RateLimitException(errorMessage);
                    }else {
                        requestCounts.get(LIMIT_KEY).add(System.currentTimeMillis());
                    }
                    return Void.TYPE; // Indicate successful execution
                })
                .flatMap(ignore -> chain.filter(exchange)); // Proceed if rate limit is not exceeded
    }

    private void cleanUpRequestCounts(final long currentTime) {
        requestCounts.values().forEach(l -> {
            l.removeIf(t -> timeIsTooOld(currentTime, t));
        });
    }

    private boolean timeIsTooOld(final long currentTime, final long timeToCheck) {
        return currentTime - timeToCheck > rateDuration;
    }

    @Override
    public void afterPropertiesSet() {
        requestCounts.put(LIMIT_KEY,new ArrayList<>());
        requestCounts.get(LIMIT_KEY).add(System.currentTimeMillis());
    }
}
