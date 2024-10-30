package com.greeting.filter



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.server.WebFilterChain
import org.springframework.web.server.handler.DefaultWebFilterChain;
import spock.lang.Specification;

@SpringBootTest
class ReactiveRequestLimitFilterSpec extends Specification {

    @Autowired
    ReactiveRequestLimitFilter reactiveRequestLimitFilter;

    def "greetingFilter"() {
        given:
        def say = "hallo"


        when:
        def reslut = reactiveRequestLimitFilter.filter(new MockServerWebExchange(),new DefaultWebFilterChain())

        then:
        reslut == null
    }

}
