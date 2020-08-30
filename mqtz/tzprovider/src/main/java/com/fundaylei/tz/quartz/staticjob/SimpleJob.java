package com.fundaylei.tz.quartz.staticjob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * （1）简单注解方式
 * 简单任务，时间写死固定
 */
@Slf4j
//@Configuration//配置类
//@EnableScheduling//开启任务调度，也可以写在主启动类上
public class SimpleJob {

    //任务计划
    @Scheduled(cron = "0/5 * * * * ?")
    public void run(){
        //任务具体执行函数
        log.debug("任务执行SimpleJob："+LocalDateTime.now());
    }

    //任务计划
    @Scheduled(cron = "0/10 * * * * ?")
    public void run2(){
        //任务具体执行函数
        log.debug("任务执行SimpleJob2："+LocalDateTime.now());
    }
}
