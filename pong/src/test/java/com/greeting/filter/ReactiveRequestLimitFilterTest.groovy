package com.greeting.filter

import org.assertj.core.util.diff.Delta
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import org.springframework.web.server.adapter.DefaultServerWebExchange
import org.springframework.web.server.handler.DefaultWebFilterChain
import reactor.core.publisher.Mono;
import spock.lang.Specification;

@SpringBootTest
class ReactiveRequestLimitFilterTest extends Specification {

    @Autowired
    ReactiveRequestLimitFilter reactiveRequestLimitFilter;

//    def "greetingFilterPass"() {
//        given:
//            WebFilterChain filterChain = filterExchange -> Mono.empty();
//            MockServerWebExchange exchange = MockServerWebExchange.from(
//                    MockServerHttpRequest
//                            .get("/greeting?say=Hello"));
//        when:
//            def reslut = reactiveRequestLimitFilter.filter(exchange,filterChain).block()
//
//        then:
//            null == reslut
//    }


    def "greetingFilterRateLimited"() {
        given:
            ReflectionTestUtils.setField(reactiveRequestLimitFilter, "rateDuration", -1);
            WebFilterChain filterChain = filterExchange -> Mono.empty();
            MockServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest
                            .get("/greeting?say=Hello"));
        when:
            reactiveRequestLimitFilter.filter(exchange,filterChain).block()
        then:
            // can not catch RateLimitException ，  not good at spock , need more learn.
            thrown(Exception)
    }

}
