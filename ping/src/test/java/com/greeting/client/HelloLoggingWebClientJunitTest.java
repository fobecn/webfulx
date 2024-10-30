//package com.greeting.client;
//
//
//import com.greeting.util.AcquireFileLockException;
//import com.greeting.util.PingFileLock;
//import com.greeting.util.PingFileLockFactory;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
///**
// * Spock 不收悉， 用junit标准先测试
// */
//@ExtendWith(MockitoExtension.class)
//public class HelloLoggingWebClientJunitTest {
//
//    @Mock
//    WebClient.Builder webClientBuilder;
//
//
//    WebClient webClient;
//
//
//    HelloLoggingWebClient helloLoggingWebClient;
//
//    @Mock
//    PingFileLock pingFileLock;
//
//
//
//    @BeforeEach
//    void setup(){
//        //webClient
//        webClient = WebClient.builder()
//                .exchangeFunction(clientRequest ->
//                        Mono.just(ClientResponse.create(HttpStatus.OK)
//                                .header("content-type", "application/json")
//                                .body("world")
//                                .build())
//                ).build();
//
//
//        // webClientBuilder mock
//        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
//        when(webClientBuilder.filter(any())).thenReturn(webClientBuilder);
//        when(webClientBuilder.build()).thenReturn(webClient);
//
//
//        helloLoggingWebClient = new HelloLoggingWebClient(webClientBuilder);
//    }
//
//    @Test
//    public void normalTest() throws AcquireFileLockException, IOException {
//        try(MockedStatic mockPingFileLockFactory = mockStatic(PingFileLockFactory.class)){
//            when(PingFileLockFactory.produce(anyString(),anyString())).thenReturn(pingFileLock);
//            when(pingFileLock.getLock()).thenReturn(true);
//            helloLoggingWebClient.sendWithFileLockLimit("hello");
//
//        }
//    }
//
//
//}
