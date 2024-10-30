package com.greeting.client;

import com.greeting.util.AcquireFileLockException;
import com.greeting.util.PingFileLock;
import com.greeting.util.PingFileLockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.io.IOException;


/**
 * @author sam
 *
 * 让webclient 具备限流功能。
 * note: 如果使用 Wrapper 来实现会更好，如果支持的话。
 */

@Component
public class HelloLoggingWebClient {
    private final static Logger log = LoggerFactory.getLogger(HelloLoggingWebClient.class);

    private WebClient webClient;

    private static final String PONG_BASE_PATH_URL = "http://localhost:8081";
    private static final String HELLO_URL = "/greeting";
    String lockFileName = "C:\\limit.lock";
    String valueFileName = "C:\\concurrent.value";

    // Create WebClient instance using builder.
    // The builder will be autoconfigured
    public HelloLoggingWebClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder // you can also just use WebClient.builder()
                .baseUrl(PONG_BASE_PATH_URL)
                .filter(logRequest()) // here is the magic
                .build();
    }

    // Sending request with request limit from file.
    public String sendWithFileLockLimit(String message) {
        try (PingFileLock fileLock = PingFileLockFactory.produce(lockFileName,valueFileName)){
            if (fileLock.getLock()){
                return send(message);
            }else {
                log.info("Sending {} failed,The request was not sent and was limited.",message);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (AcquireFileLockException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    private String send(String message) {
        log.info("Sending: {}", message);
        ClientResponse clientResponse = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(HELLO_URL)
                        .queryParam("say", message)
                        .build())
                .exchange()
                .block();
        String response = String.valueOf(clientResponse.toEntity(String.class).block());
        log.info("Response: {}", response);
        return response;
    }

    // This method returns filter function which will log request data
    public static ExchangeFilterFunction logRequest() {
        //jacoco can not be coveraged!!!!
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
}
