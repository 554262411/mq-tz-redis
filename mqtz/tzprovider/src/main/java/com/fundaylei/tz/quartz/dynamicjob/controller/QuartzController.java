package com.fundaylei.tz.quartz.dynamicjob.controller;

import com.fundaylei.tz.quartz.dynamicjob.model.SchedulerJob;
import com.fundaylei.tz.quartz.dynamicjob.Service.QuartzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体操作的控制类，可以可页面进行集成
 * 1、任务创建之后，页面需要控制到只有cron表达式和相关的描述信息可以修改，其他的不能修改
 * 2、向数据库表中插入的业务逻辑
 * 3、从数据库表中删除的业务逻辑
 * 4、从数据库表中获取所有的任务列表
 * 5、同样需要一个定时任务，刷新页面列表中各个任务的状态信息
 *
 * 编写一个Controller类
 * 任务队列操作的入口，可以通过postman或者swagger或者做页面的管理进行模拟操作
 * 对服务创建、启动、修改、暂停、重启、删除、获取
 * 示例：http://localhost:8082/swagger-ui.html
     {
         "classname": "com.fundaylei.tz.quartz.dynamicjob.demo.TestCustomJob",
         "createtime": "2020-06-27T09:26:10.970Z",
         "cronexpression": "0/5 * * * * ?",
         "description": "任务队列",
         "enable": true,
         "jobgroup": "group1",
         "jobid": 1,
         "jobname": "job1",
         "lastupdatetime": "2020-06-27T09:26:10.970Z",
         "owner": "string",
         "pause": true,
         "triggergroup": "triggergroup1",
         "triggername": "triggername1"
     }
 */
@SuppressWarnings("deprecation")
@RestController
@RequestMapping("/quartz")
@Api(tags="Quartz测试")
public class QuartzController {

    /**
     * 注入具体的操作类
     */
    @Autowired
    private QuartzService quartzService;

    /////////页面控制逻辑的控制器/////////////////////////////////////////////////////////////

    /**
     * 【****应用程序启动时，初始化工作****】
     * 初始化Job工作，在应用程序启动时调用一次，初始化所有的job任务队列
     * 设置自启动的，则会自动启动任务队列
     * @throws Exception
     */
    public void initJobs() throws Exception {
        List<SchedulerJob> jobList = getJobList();
        quartzService.initJobs(jobList);
    }

    @PostMapping("/getJobList")
    @ApiOperation("获取所有的任务列表")
    public List<SchedulerJob> getJobList() throws Exception{
        //TODO:从数据库表中获取所有的任务列表
        List<SchedulerJob> jobList=new ArrayList<>();
        //----

        return jobList;
    }

    @PostMapping("/getJobListWithState")
    @ApiOperation("获取所有的任务列表，带有任务状态信息")
    public List<SchedulerJob> getJobListWithState() throws Exception {
        List<SchedulerJob> jobList = getJobList();
        for (SchedulerJob job : jobList) {
            //获取任务状态
            Trigger.TriggerState state = quartzService.getTimerJobState(job);
            job.setJobstate(state.toString());
        }
        return jobList;
    }

    @PostMapping("/runAllJob")
    @ApiOperation("启动所有任务队列job")
    public void runAllJob() throws Exception {
        List<SchedulerJob> jobList = getJobList();
        for (SchedulerJob job : jobList) {
            //判断任务状态，未启动的需要启动
            Trigger.TriggerState state = quartzService.getTimerJobState(job);
            if (state != Trigger.TriggerState.NORMAL) {
                resumeJob(job);
            }
        }
    }

    @PostMapping("/pauseAllJob")
    @ApiOperation("暂停所有任务队列job")
    public void pauseAllJob() throws Exception {
        List<SchedulerJob> jobList = getJobList();
        for (SchedulerJob job : jobList) {
            //判断任务状态，启动的需要暂停
            Trigger.TriggerState state = quartzService.getTimerJobState(job);
            if (state == Trigger.TriggerState.NORMAL) {
                pauseJob(job);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////

    @PostMapping("/add")
    @ApiOperation("创建Quartz任务，默认创建完成后不自动启动")
    public String addJob(@RequestBody SchedulerJob job) throws Exception{
        if(job==null) throw new Exception("job is null");
        quartzService.addTimerJob(job);
        //TODO:向数据库表中插入的业务逻辑

        return "创建Quartz任务成功";
    }

    @PostMapping("/singlerun")
    @ApiOperation("单次执行一次Quartz任务")
    public String singleRunJob(@RequestBody SchedulerJob job) throws Exception{
        if(job==null) throw new Exception("job is null");
        quartzService.singleRunTimerJob(job);
        return "单次执行Quartz任务成功";
    }

    @PostMapping("/update")
    @ApiOperation("修改Quartz任务,修改Job触发器条件cron表达式")
    public String updateJob(@RequestBody SchedulerJob job) throws Exception{
        if(job==null) throw new Exception("job is null");
        quartzService.updateTimerJob(job);
        return "修改Quartz任务,修改Job触发器条件cron成功";
    }

    @PostMapping("/pause")
    @ApiOperation("暂停Quartz任务")
    public String pauseJob(@RequestBody SchedulerJob job) throws Exception{
        if(job==null) throw new Exception("job is null");
        quartzService.pauseTimerJob(job);
        return "暂停Quartz任务成功";
    }

    @PostMapping("/resume")
    @ApiOperation("启动或重新唤醒Quartz任务")
    public String resumeJob(@RequestBody SchedulerJob job) throws Exception{
        if(job==null) throw new Exception("job is null");
        quartzService.resumeTimerJob(job);
        return "重启或重新唤醒Quartz任务成功";
    }

    @PostMapping("/delete")
    @ApiOperation("删除Quartz任务")
    public String deleteJob(@RequestBody SchedulerJob job) throws Exception{
        if(job==null) throw new Exception("job is null");
        quartzService.deleteTimerJob(job);
        //TODO:从数据库表中删除的业务逻辑

        return "删除Quartz任务成功";
    }

    @PostMapping("/select")
    @ApiOperation("获取Quartz任务的具体描述信息")
    public String selectJob(@RequestBody SchedulerJob job) throws Exception{
        if(job==null) throw new Exception("job is null");
        String selectTimerJob = quartzService.selectTimerJob(job);
        return selectTimerJob;
    }

}
