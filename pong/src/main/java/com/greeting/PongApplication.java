package com.greeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sam
 * @date 2024-10-28
 *
 * Code Challenge
 * Imaging a simplified scenario where you are asked to implement 2 microservices
 * called “'ping’respectively and integrates with each other as illustrated belowand“pong
 * Both services must be irple!ted using Soring WebFlux and can be packaged & run as executable
 * iarBoth services are running locali in same device.
 * The integration goes as simple as -Ping Service atempts to says "Hello" to Pong Service aroundevery / second and thenPong service should respond with "world"The Pong Service should be implemented with aThroitlimg Control limited with value 1.meaning thatFor any given second, there is onlyests can be handled by it.I in the given second, Pong Service should return 429 status code0tioseaiti0na Teuests comiMoliple Ping Services shonld be runmingas separate VM Proces with capabily of&ale Limit conrol acros al proceseswith ony2Rps (hint consider using ava FfileLock), meaning that.If all processes attempt to triggers Pong Service at the same time. only 2 requests are allowed to go out to Pong Service.Among the 2 outgoing reguests to Pong, ifthey reach Pong Service within the same second, one ofthem are expected to be throtled with 429 status codeEach Ping service process must log the request attempt with result () in separate logs per instance for review. The result includes
 * Request sent & Pong Respond
 * Request not send as being "rate limitedReouest send &, Pong throttled it.Increase the number
 * ofrunning Ping processes locally and review the logs for each
 * Code Quality Acceptance Criteria:
 * Using Spring Boot and Spring Webflux Framework is a must.
 * Using Spring Spock Framewvork in Groowy for Unit Test.
 * Unit Test with Coverage >= 80%. (hint: Maven Jacoco Plugin should be used)Completion of the Challenge should not take longer than 1 week.
 */
@SpringBootApplication
public class PongApplication {
    public static void main(String[] args) {
        SpringApplication.run(PongApplication.class, args);
    }
}
