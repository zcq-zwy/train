package com.zcqzwy.batch.service;

import com.zcqzwy.batch.req.CronJobReq;
import com.zcqzwy.batch.resp.CronJobResp;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CronJobService {

  private static final Logger LOG = LoggerFactory.getLogger(CronJobService.class);

  // 直接注入 Scheduler，而不是 FactoryBean，Spring Boot 自动配置好了
  @Autowired
  private Scheduler scheduler;

  /**
   * 手动立即执行一次任务
   */
  public void runJob(CronJobReq req) throws SchedulerException {
    scheduler.triggerJob(JobKey.jobKey(req.getName(), req.getGroup()));
  }

  /**
   * 添加/创建定时任务
   */
  public void addJob(CronJobReq req) throws SchedulerException, ClassNotFoundException {
    // 启动调度器 (通常Spring启动时已自动启动，这里保留原逻辑确保万一)
    if (!scheduler.isStarted()) {
      scheduler.start();
    }

    // 构建job信息
    Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(req.getName());
    JobDetail jobDetail = JobBuilder.newJob(jobClass)
        .withIdentity(req.getName(), req.getGroup())
        .build();

    // 表达式调度构建器
    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(req.getCronExpression());

    // 构建 trigger
    CronTrigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(req.getName(), req.getGroup())
        .withDescription(req.getDescription())
        .withSchedule(scheduleBuilder)
        .build();

    scheduler.scheduleJob(jobDetail, trigger);
  }

  /**
   * 暂停任务
   */
  public void pauseJob(CronJobReq req) throws SchedulerException {
    scheduler.pauseJob(JobKey.jobKey(req.getName(), req.getGroup()));
  }

  /**
   * 恢复任务
   */
  public void resumeJob(CronJobReq req) throws SchedulerException {
    scheduler.resumeJob(JobKey.jobKey(req.getName(), req.getGroup()));
  }

  /**
   * 更新任务 (重调度)
   */
  public void rescheduleJob(CronJobReq req) throws SchedulerException {
    TriggerKey triggerKey = TriggerKey.triggerKey(req.getName(), req.getGroup());

    // 获取旧的 Trigger
    CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    if (oldTrigger == null) {
      throw new SchedulerException("未找到触发器: " + req.getName());
    }

    // 表达式调度构建器
    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(req.getCronExpression());

    // 按新的cronExpression表达式重新构建trigger
    CronTrigger newTrigger = oldTrigger.getTriggerBuilder()
        .withIdentity(triggerKey)
        .withDescription(req.getDescription())
        .withSchedule(scheduleBuilder)
        .startAt(new Date()) // 重置开始时间
        .build();

    // 按新的trigger重新设置job执行
    scheduler.rescheduleJob(triggerKey, newTrigger);
  }

  /**
   * 删除任务
   */
  public void deleteJob(CronJobReq req) throws SchedulerException {
    JobKey jobKey = JobKey.jobKey(req.getName(), req.getGroup());
    TriggerKey triggerKey = TriggerKey.triggerKey(req.getName(), req.getGroup());

    scheduler.pauseTrigger(triggerKey);
    scheduler.unscheduleJob(triggerKey);
    scheduler.deleteJob(jobKey);
  }

  /**
   * 查询所有任务
   */
  public List<CronJobResp> queryJobs() throws SchedulerException {
    List<CronJobResp> cronJobDtoList = new ArrayList<>();

    for (String groupName : scheduler.getJobGroupNames()) {
      for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
        CronJobResp cronJobResp = new CronJobResp();
        cronJobResp.setName(jobKey.getName());
        cronJobResp.setGroup(jobKey.getGroup());

        // 获取 Job 的 Trigger
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        if (triggers != null && !triggers.isEmpty()) {
          CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
          cronJobResp.setNextFireTime(cronTrigger.getNextFireTime());
          cronJobResp.setPreFireTime(cronTrigger.getPreviousFireTime());
          cronJobResp.setCronExpression(cronTrigger.getCronExpression());
          cronJobResp.setDescription(cronTrigger.getDescription());

          Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
          cronJobResp.setState(triggerState.name());
        }

        cronJobDtoList.add(cronJobResp);
      }
    }
    return cronJobDtoList;
  }
}