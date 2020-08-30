package com.fundaylei.tz.quartz.staticjob;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;


/**
 * （3）统一管理方式：同xml的配置方式
 * 配置类：示例
 */
@Slf4j
//@Configuration
public class XmlConfigJob {

    /**
     * 【JobDetail-1】
     * 创建工厂bean,实现自定义JobDetail
     * @return
     */
    @Bean
    public MethodInvokingJobDetailFactoryBean createFactoryBean(){
        MethodInvokingJobDetailFactoryBean factoryBean = new MethodInvokingJobDetailFactoryBean();
        //设置执行对象，注入的bean对象
        factoryBean.setTargetBeanName("myQuartzTask");
        //设置执行方法，bean对象中的具体执行的方法
        factoryBean.setTargetMethod("quartzTask");
        //设置任务是否并发执行，true：任务并发执行，false：上一个任务执行完，下一个任务才开始执行
        factoryBean.setConcurrent(true);
        return factoryBean;
    }

    /**
     * 【JobDetail-2】
     * @return
     */
    @Bean
    public MethodInvokingJobDetailFactoryBean createFactoryBean2(){
        MethodInvokingJobDetailFactoryBean factoryBean = new MethodInvokingJobDetailFactoryBean();
        //设置执行对象，注入的bean对象
        factoryBean.setTargetBeanName("myQuartzTask2");
        //设置执行方法，bean对象中的具体执行的方法
        factoryBean.setTargetMethod("quartzTask");
        //设置任务是否并发执行，true：任务并发执行，false：上一个任务执行完，下一个任务才开始执行
        factoryBean.setConcurrent(true);
        return factoryBean;
    }


    /**
     * 【触发器-1】
     * 通过工厂bean创建trigger触发器
     *
     * 参数注入：
     * 有参数factoryBean，若spring容器中只有一个MethodInvokingJobDetailFactoryBean 类型的bean，
     * 则不论参数取名为何都是按类型取bean ConnectionFactory 为参数，
     * 若有多个则参数取名必须为多个bean中的一个，否则报错。
     * 因此多个Bean实例时，传递参数的命名需要通过@Qualifier(value="createFactoryBean")来指定
     * @param factoryBean
     * @return
     * @throws ParseException
     */
    @Bean
    //当只有一个参数时，可以这样定义，不用指定名称，会根据类型自动注入
    //public CronTrigger cronTrigger(MethodInvokingJobDetailFactoryBean factoryBean) throws ParseException{
    public CronTrigger cronTrigger(@Qualifier(value="createFactoryBean") MethodInvokingJobDetailFactoryBean factoryBean) throws ParseException{
        CronTriggerFactoryBean triggerBean = new CronTriggerFactoryBean();
        //设置trigger所属的工厂bean，加入相关的执行类和方法
        triggerBean.setJobDetail(factoryBean.getObject());
        //设置执行周期
        triggerBean.setCronExpression("0/3 * * * * ?");
        //triggerBean.setName("customTrigger");
        //在bean属性填充完成后，执行初始化，如果配置出错则抛出异常
        triggerBean.afterPropertiesSet();
        return triggerBean.getObject();
    }

    /**
     * 【触发器-2】
     * @param factoryBean
     * @return
     * @throws ParseException
     */
    @Bean
    public CronTrigger cronTrigger2(@Qualifier(value="createFactoryBean2") MethodInvokingJobDetailFactoryBean factoryBean) throws ParseException{
        CronTriggerFactoryBean triggerBean = new CronTriggerFactoryBean();
        //设置trigger所属的工厂bean，加入相关的执行类和方法
        triggerBean.setJobDetail(factoryBean.getObject());
        //设置执行周期
        triggerBean.setCronExpression("0/5 * * * * ?");
        //triggerBean.setName("customTrigger");
        //在bean属性填充完成后，执行初始化，如果配置出错则抛出异常
        triggerBean.afterPropertiesSet();
        return triggerBean.getObject();
    }

    /**
     * 【任务调度】任务调度两个定时器
     * 创建调度工厂类，自动注册trigger
     *
     * 三个点说明
     * 在Java1.5之后在方法上传参时便出现了**"…"**,这三个点的学名叫做可变长参数,也就是相当于一个数组,能够传入0个至n个参数
     * @param trigger1
     * @return
     */
    @Bean
    //只有一个触发器的话，可以这样子写
    //public SchedulerFactoryBean schedulerFactoryBean(CronTrigger... triggers){
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier(value="cronTrigger") CronTrigger trigger1
            ,@Qualifier(value="cronTrigger2") CronTrigger trigger2){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        //当triggers为null时，报异常
        factoryBean.setTriggers(trigger1,trigger2);
        return factoryBean;
    }

    @Component("myQuartzTask")
    public class QuartzTask{
        public void quartzTask() {
            DateFormatter df = new DateFormatter("yyyyMMdd HH:mm:ss");
            System.out.println("["+Thread.currentThread().getName()+"]执行任务XmlConfigJob，当前时间："+df.print(new Date(), Locale.CHINA));
        }
    }

    @Component("myQuartzTask2")
    public class QuartzTask2{
        public void quartzTask() {
            DateFormatter df = new DateFormatter("yyyyMMdd HH:mm:ss");
            System.out.println("["+Thread.currentThread().getName()+"]执行任务XmlConfigJob-2，当前时间："+df.print(new Date(), Locale.CHINA));
        }
    }


}

