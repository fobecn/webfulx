package com.greeting.controller;

import com.greeting.filter.ReactiveRequestLimitFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


/**
 * @author Sam
 *
 */
@RestController
public class PongController {

    private final Logger log = LoggerFactory.getLogger(PongController.class);

    @GetMapping("/greeting")
    public Mono<String> greeting(@RequestParam(required = false)String say) {   // 【改】返回类型为Mono<String>
        log.info("PongController Received {}",say);
        return Mono.just("world");     // 【改】使用Mono.just生成响应式数据
    }
}
