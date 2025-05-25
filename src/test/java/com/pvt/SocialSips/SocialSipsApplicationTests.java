package com.pvt.SocialSips;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {SocialSipsApplication.class})
@SpringJUnitConfig(classes = ContextConfiguration.class)
class SocialSipsApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @BeforeTestClass
    void contextLoads() {
        assertThat(applicationContext.getBean(SocialSipsApplication.class)).isNotNull();
    }



}
