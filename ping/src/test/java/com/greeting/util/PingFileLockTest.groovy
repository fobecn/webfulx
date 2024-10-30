package com.greeting.util

import com.greeting.schedule.HalloSchedule
import org.mockito.Spy
import spock.lang.Specification

class PingFileLockTest extends Specification{

    @Spy
    PingFileLock halloSchedule = Spy(PingFileLock)


//    def "out of limit test"() {
//        given:
//        PingFileLock
//        when:
//        halloSchedule.hallo();
//        then:
//        1 * helloLoggingWebClient.sendWithFileLockLimit("hello")
//    }

}
