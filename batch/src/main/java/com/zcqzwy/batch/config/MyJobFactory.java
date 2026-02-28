package com.zcqzwy.batch.config;

import jakarta.annotation.Resource;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author 29282
 * 默认情况下，当定时任务触发时，Quartz 是通过反射（class.newInstance()）自己去 new 一个 Job 对象的。
 * 问题： 这个对象是 Quartz new 出来的，不是 Spring 容器创建的。
 * 后果： Spring 根本不知道这个对象的存在，所以无法在这个对象里进行依赖注入。
 * 如果你在 Job 类里写了 @Autowired MyService myService;，运行时你会发现 myService 是 null，报空指针异常。
 */
@Component
public class MyJobFactory extends SpringBeanJobFactory {

    @Resource
    private AutowireCapableBeanFactory beanFactory;

    /**
     * 这里覆盖了super的createJobInstance方法，对其创建出来的类再进行autowire。
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        // 这一步会扫描 jobInstance 里的 @Autowired 注解，并从 Spring 容器里找到对应的 Bean 注入进去
        beanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
