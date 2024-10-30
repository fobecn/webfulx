package com.greeting.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import spock.lang.Specification;

/**
 * 修复groovy不编译执行问题。
 *
 * 实际项目需要maven 加入groovy 的编译兼容
 */
@SpringBootTest
public class GroovySpec extends Specification {

    @Test
    public void test(){
        Assertions.assertEquals(1,1);
    }
}
