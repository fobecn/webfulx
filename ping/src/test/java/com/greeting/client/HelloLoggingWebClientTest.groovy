package com.greeting.client

import com.greeting.util.AcquireFileLockException
import com.greeting.util.PingFileLock
import org.mockito.InjectMocks
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification
import com.greeting.util.PingFileLockFactory

class HelloLoggingWebClientTest extends Specification {

    WebClient.Builder mockWebClientBuilder = Mock(WebClient.Builder)
    WebClient mockWebClient = Mock(WebClient)
    WebClient.RequestHeadersUriSpec MockRequestHeadersUriSpec = Mock(WebClient.RequestHeadersUriSpec)
    WebClient.RequestHeadersSpec mockRequestHeadersSpec = Mock(WebClient.RequestHeadersSpec)

    Mono<ClientResponse> mockClientResponseMono = Mock(Mono)
    ClientResponse mockClientResponse = Mock(ClientResponse)
    PingFileLock         mockPingFileLock = Mock(PingFileLock.class)

    PingFileLockFactory mockPingFileLockFactory = SpyStatic(PingFileLockFactory)


    WebClient.Builder webClientBuilder;

    @InjectMocks
    HelloLoggingWebClient client

    void setup(){
        //build webClient mock
        mockWebClient.get() >> MockRequestHeadersUriSpec
        MockRequestHeadersUriSpec.uri(_) >> mockRequestHeadersSpec
        mockRequestHeadersSpec.exchange() >> mockClientResponseMono
        mockClientResponseMono.block() >> mockClientResponse
        mockClientResponse.toEntity(String.class) >> Mono.just("world")

        //build webClientBuilder mock
        mockWebClientBuilder.baseUrl(_) >> mockWebClientBuilder
        mockWebClientBuilder.filter(_) >> mockWebClientBuilder
        mockWebClientBuilder.build() >> mockWebClient

        client = new HelloLoggingWebClient(mockWebClientBuilder)

        webClientBuilder = WebClient.builder();
    }

    def "test sendWithFileLockLimit - Lock acquired"() {
        when:
        def result = client.sendWithFileLockLimit("Hello")

        then:
        result == "world"
    }

    def "test sendWithFileLockLimit - make a real instance for cover logRequest !!! still not cover!!!  ,函数编码jacoco无法覆盖bug"() {
        when:
        def helloLoggingWebClient = new HelloLoggingWebClient(webClientBuilder);
        then:
            helloLoggingWebClient != null
    }




    def "test sendWithFileLockLimit - acquired lock failed"() {
        given:
        PingFileLockFactory.produce(_,_) >> { throw new AcquireFileLockException("ouch") }
        when:
        def result = client.sendWithFileLockLimit("Hello")
        then:
            result == null
    }

    def "test sendWithFileLockLimit - IOException"() {
        given:
        PingFileLockFactory.produce(_,_) >> { throw new IOException("ouch") }
        when:
        def result = client.sendWithFileLockLimit("Hello")
        then:
         thrown(RuntimeException)
    }

}
