package com.ecomm.springcloudgateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringCloudGatewayApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
        assertThat(environment.getProperty("spring.application.name")).isEqualTo("spring-cloud-gateway");
    }

}
