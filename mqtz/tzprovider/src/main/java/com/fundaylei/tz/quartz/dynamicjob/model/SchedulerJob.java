package com.fundaylei.tz.quartz.dynamicjob.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 数据库实体模型
 * 创建一个Job实体类，@Data用于存放Job的各种信息（set，get方法省略）
 */
@ApiModel
@Data
public class SchedulerJob {

    @ApiModelProperty(value="jobid",dataType="String",name="jobid",example="1")
    private String jobid;

    @ApiModelProperty(value="classname",dataType="String",name="classname",example="com.fundaylei.tz.quartz.dynamicjob.demo.TestCustomJob")
    private String classname;

    @ApiModelProperty(value="cronexpression",dataType="String",name="cronexpression",example="0/5 * * * * ?")
    private String cronexpression;

    ///////////////////////////
    @ApiModelProperty(value="jobname",dataType="String",name="jobname",example="job1")
    private String jobname;

    @ApiModelProperty(value="jobgroup",dataType="String",name="jobgroup",example="group1")
    private String jobgroup;

    ///////////////////////////

    @ApiModelProperty(value="triggername",dataType="String",name="triggername",example="triggername1")
    private String triggername;

    @ApiModelProperty(value="triggergroup",dataType="String",name="triggergroup",example="triggergroup1")
    private String triggergroup;

    ///////////////////////////

    @ApiModelProperty(value="pause",dataType="Boolean",name="pause",example="true")
    private Boolean pause;

    private Boolean enable;

    @ApiModelProperty(value="description",dataType="String",name="description",example="任务队列")
    private String description;

    private Date createtime;

    private Date lastupdatetime;

    private String owner;

    /**
     * 是否自动启动
     */
    @ApiModelProperty(value="autorun",dataType="Boolean",name="autorun",example="true")
    private Boolean autorun;

    /**
     * 任务状态
     */
    @ApiModelProperty(value="jobstate",dataType="String",name="jobstate",example="NONE,NORMAL,PAUSED,COMPLETE,ERROR,BLOCKED")
    private String jobstate;

}
