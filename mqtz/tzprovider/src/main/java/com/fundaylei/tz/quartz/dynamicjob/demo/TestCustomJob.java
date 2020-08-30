package com.fundaylei.tz.quartz.dynamicjob.demo;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 编写需要调度的执行类，这也是我们日常开发业务代码的位置
 * 定义任务具体的执行类，通过反射机制进行执行的
 */
@Component
public class TestCustomJob implements Job {

    private void before() {
        System.out.println("开始执行");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        before();
        //业务逻辑

        //获取job id
        String name = context.getJobDetail().getKey().getName();
        System.out.println("[" + Thread.currentThread().getName() + "],执行Job" + name + ",当前时间：" + new Date());


        after();
    }

    private void after() {
        System.out.println("执行结束");
    }

}
