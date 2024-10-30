package com.greeting.util

import spock.lang.Specification

class PingFileLockFactoryTest extends Specification{

    def "get instance"() {
        when:
            def instance = PingFileLockFactory.instance
        then:
            instance != null
    }
}
