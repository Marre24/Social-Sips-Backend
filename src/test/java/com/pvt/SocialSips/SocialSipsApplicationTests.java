package com.pvt.SocialSips;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@PropertySource("classpath:application-test.properties")
@SpringBootTest(properties =
        {
                "spring.datasource.password=password",
                "spring.datasource.username=test", "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver"
        })
class SocialSipsApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext.getBean(SocialSipsApplication.class)).isNotNull();
    }



}
