package com.greeting.controller


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification


@SpringBootTest
class PongControllerSpec extends Specification {

    @Autowired
    PongController pongController;

    @Autowired
    MockMvc mockMvc;

    def "greeting"() {
        expect:
            "world" == pongController.greeting("say")
    }

//    def "simple"() {
//        expect:
//            mockMvc.perform(MockMvcRequestBuilders.get("/greeting?say=hallo"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string("world"))
//    }




}