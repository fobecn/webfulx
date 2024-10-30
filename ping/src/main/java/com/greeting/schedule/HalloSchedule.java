package com.greeting.schedule;

import com.greeting.client.HelloLoggingWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HalloSchedule {

    @Autowired
    private HelloLoggingWebClient helloLoggingWebClient;

    @Scheduled(fixedRate = 500)  //There will be a delay in sending, resulting in more than 2 requests per second
//    @Scheduled(fixedDelay = 500)
    public void hallo() {
        helloLoggingWebClient.sendWithFileLockLimit("hello");
    }

}
