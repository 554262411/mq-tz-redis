package com.fundaylei.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MqProviderApplication {
    public static void main(String[] args) {
        log.debug("我启动了MqProviderApplication");
        SpringApplication.run(MqProviderApplication.class,args);
    }
}
