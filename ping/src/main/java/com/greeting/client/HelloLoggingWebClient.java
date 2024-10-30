package com.greeting.client;

import com.greeting.util.AcquireFileLockException;
import com.greeting.util.PingFileLock;
import com.greeting.util.PingFileLockFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.io.IOException;


@Slf4j
@Component
public class HelloLoggingWebClient {

    private final WebClient webClient;

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
    public void sendWithFileLockLimit(String message) {
        //
        try (PingFileLock fileLock = PingFileLockFactory.produce(lockFileName,valueFileName)){
            if (fileLock.getLock()){
                send(message);
            }else {
                log.info("The request was not sent and was limited.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (AcquireFileLockException e) {
            log.error(e.getMessage());
        }
    }


    public void send(String message) {
        log.info("Sending: {}", message);
        ClientResponse clientResponse = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(HELLO_URL)
                        .queryParam("say", message)
                        .build())
                .exchange()
                .block();
        log.info("Response: {}", clientResponse.toEntity(String.class).block());
    }

    // This method returns filter function which will log request data
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
}
