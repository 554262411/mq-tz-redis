package com.fundaylei.tz.quartz.staticjob;

import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;//CronTrigger
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * （2）扩展接口方式
 * 复杂任务，时间从数据库表中读取
 */
@Slf4j
//@Configuration
public class ComplexJob implements SchedulingConfigurer {

    //引用dao层，从数据库中读取cron定义
    //数据库中修改，可以动态更改任务时间

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

        scheduledTaskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                //任务具体执行函数
                log.debug("任务执行ComplexJob："+ LocalDateTime.now());
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {

                //从数据库中读取
                String cron="0/5 * * * * ?";
                CronTrigger cronTrigger = new CronTrigger(cron);
                Date nextExec = cronTrigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        });

    }
}
