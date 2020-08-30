package com.fundaylei.tz.quartz.dynamicjob.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author fundaylei
 *	Job工厂
 *创建一个Job工厂，用于将Job实例注入到Job工厂中
 */
@Component
public class JobFactory extends AdaptableJobFactory{

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle)
            throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        //将Job实例注入到Job工厂
        beanFactory.autowireBean(jobInstance);
        return jobInstance;
    }

}

