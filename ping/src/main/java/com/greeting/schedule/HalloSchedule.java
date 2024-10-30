package com.greeting.schedule;

import com.greeting.client.HelloLoggingWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author sam
 * @date 2024-10-29
 */

@Component
public class HalloSchedule {

    @Autowired
    private HelloLoggingWebClient helloLoggingWebClient;

//    public HalloSchedule(HelloLoggingWebClient helloLoggingWebClient) {
//        this.helloLoggingWebClient = helloLoggingWebClient;
//    }

    @Scheduled(fixedRate = 500)  //send 2 request per seconds
    public void hallo() {
        helloLoggingWebClient.sendWithFileLockLimit("hello");
    }

}
