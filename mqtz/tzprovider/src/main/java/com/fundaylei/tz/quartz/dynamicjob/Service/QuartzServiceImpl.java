package com.fundaylei.tz.quartz.dynamicjob.Service;

import com.fundaylei.tz.quartz.dynamicjob.model.SchedulerJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Quartz任务实现类
 * 编写QuartzService接口实现类
 *
 * 至此，我们已经实现了动态创建任务的功能，并且还实现了动态修改任务周期，暂停任务，唤醒任务，删除任务以及查询任务执行状态等信息。
 * 我们只需要封装并传入一个SchedulerJob实体类（存放Job的相关信息）即可。
 * @author
 *
 */
@Service
public class QuartzServiceImpl implements QuartzService {

    /**
     * 把任务调度配置类QuartzConfig中的调度实体对象scheduler注入进来
     * 根据调度类工厂bean获取调度实体对象
     */
    @Autowired
    @Qualifier("scheduler")
    private Scheduler scheduler;

    /**
     * 【应用程序启动时，初始化工作】
     * 初始化Job工作，在应用程序启动时调用一次，初始化所有的job任务队列
     * 设置自启动的，则会自动启动任务队列
     * @param jobList
     */
    @Override
    public void initJobs(List<SchedulerJob> jobList){
        for(SchedulerJob job : jobList) {
            //加入任务队列
            addTimerJob(job);
            //如果设置为自启动的，则启动
            if(job.getAutorun()){
                resumeTimerJob(job);
            }
        }
    }

    /**
     * 创建Job
     * @param job
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addTimerJob(SchedulerJob job) {
        try {
            //创建job
            JobDetail jobDetail = JobBuilder
                    // 指定执行类
                    .newJob((Class<? extends Job>) Class.forName(job.getClassname()))
                    // 指定name和group，名成和分组是任务的唯一性标识
                    .withIdentity(job.getJobname(), job.getJobgroup())
                    .requestRecovery().withDescription(job.getDescription())
                    .build();
            // 创建表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                    .cronSchedule(job.getCronexpression());
            // 创建触发器，设定表达式
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(job.getTriggername(), job.getTriggergroup())
                    .withSchedule(cronScheduleBuilder).build();
            //加入任务队列
            scheduler.scheduleJob(jobDetail, cronTrigger);
            //添加任务时，先任务处于暂停状态
            //scheduler.start();//开启任务， 理论上，触发器并不会立刻触发，而实际会立即执行
            scheduler.pauseJob(JobKey.jobKey(job.getJobname(), job.getJobgroup()));
            System.out.println("==================================创建Job成功！==================================");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单次执行一次Job
     * @param job
     */
    @Override
    public void singleRunTimerJob(SchedulerJob job) {
        try {
            scheduler.triggerJob(JobKey.jobKey(job.getJobname(), job.getJobgroup()));
            System.out.println("==================================单次Job执行成功！==================================");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改Job触发器条件cron表达式
     * @param job
     */
    @Override
    public void updateTimerJob(SchedulerJob job) {
        try {
            //获取当前任务的触发器
            TriggerKey triggerKey = new TriggerKey(job.getTriggername(), job.getTriggergroup());
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 重新构件表达式，设定到触发器上
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronexpression());
            CronTrigger trigger = cronTrigger.getTriggerBuilder()
                    .withIdentity(triggerKey).withSchedule(cronScheduleBuilder)
                    .build();
            //更新触发器
            scheduler.rescheduleJob(triggerKey, trigger);
            System.out.println("==================================更新Job的Cron表达式成功！==================================");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停Job
     * @param job
     */
    @Override
    public void pauseTimerJob(SchedulerJob job) {
        try {
            scheduler.pauseJob(JobKey.jobKey(job.getJobname(), job.getJobgroup()));
            System.out.println("==================================暂停Job！==================================");
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 唤醒、启动Job
     * @param job
     */
    @Override
    public void resumeTimerJob(SchedulerJob job) {
        try {
            scheduler.resumeJob(JobKey.jobKey(job.getJobname(), job.getJobgroup()));
            System.out.println("==================================重启Job！==================================");
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 删除Job
     * @param job
     */
    @Override
    public void deleteTimerJob(SchedulerJob job) {
        try {
            scheduler.deleteJob(JobKey.jobKey(job.getJobname(), job.getJobgroup()));
            System.out.println("==================================删除Job！==================================");
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取Job的具体描述信息
     * @param job
     * @return
     */
    @Override
    public String selectTimerJob(SchedulerJob job) {
        TriggerKey triggerKey = new TriggerKey(job.getTriggername(), job.getTriggergroup());
        try {
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            return "expression:" + cronTrigger.getCronExpression()
                    + ",description:" + cronTrigger.getDescription()
                    + ",state:" + scheduler.getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Job的执行状态
     * @param job
     * @return
     */
    @Override
    public Trigger.TriggerState getTimerJobState(SchedulerJob job) {
        TriggerKey triggerKey = new TriggerKey(job.getTriggername(), job.getTriggergroup());
        try {
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            return scheduler.getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

}
