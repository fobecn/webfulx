package com.greeting.controller


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification


@SpringBootTest
class PongControllerTest extends Specification {

    @Autowired
    PongController pongController;


    def "greeting"() {
        expect:
            "world" == pongController.greeting("say")
    }






}