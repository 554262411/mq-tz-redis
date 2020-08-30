package com.fundaylei.tz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling//开启任务调度
@SpringBootApplication
public class TzProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(TzProviderApplication.class,args);
    }
}
