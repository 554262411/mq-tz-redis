package com.fundaylei.tz.quartz.dynamicjob.Service;

import com.fundaylei.tz.quartz.dynamicjob.model.SchedulerJob;
import org.quartz.Trigger;

import java.util.List;

/**
 * Quartz任务接口
 * @author fundaylei
 * 编写QuartzService接口，定义需要实现的功能
 * 对任务队列的具体操作
 */
public interface QuartzService {

    /**
     * 【应用程序启动时，初始化工作】
     * 初始化Job工作，在应用程序启动时调用一次，初始化所有的job任务队列
     * 设置自启动的，则会自动启动任务队列
     * @param jobList
     */
    void initJobs(List<SchedulerJob> jobList);

    /**
     * 创建Job
     * @param job
     */
    void addTimerJob(SchedulerJob job);

    /**
     * 单次执行一次Job
     * @param job
     */
    void singleRunTimerJob(SchedulerJob job);

    /**
     * 修改Job
     * @param job
     */
    void updateTimerJob(SchedulerJob job);

    /**
     * 暂定Job
     * @param job
     */
    void pauseTimerJob(SchedulerJob job);

    /**
     * 唤醒、启动Job
     * @param job
     */
    void resumeTimerJob(SchedulerJob job);

    /**
     * 删除Job
     * @param job
     */
    void deleteTimerJob(SchedulerJob job);

    /**
     * 获取Job
     * @param job
     */
    String selectTimerJob(SchedulerJob job);

    /**
     * 获取Job执行状态
     * @param job
     */
    Trigger.TriggerState getTimerJobState(SchedulerJob job);
}
