package com.zcqzwy.batch.controller;

import com.zcqzwy.batch.req.CronJobReq;
import com.zcqzwy.batch.resp.CronJobResp;
import com.zcqzwy.batch.service.CronJobService;
import com.zcqzwy.common.resp.CommonResp;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 29282
 */
@RestController
@RequestMapping(value = "/admin/job")
public class JobController {

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private CronJobService cronJobService;

    @RequestMapping(value = "/run")
    public CommonResp<Object> run(@RequestBody CronJobReq cronJobReq) {
        LOG.info("手动执行任务开始：{}, {}", cronJobReq.getName(), cronJobReq.getGroup());
        CommonResp<Object> commonResp = new CommonResp<>();
        try {
            cronJobService.runJob(cronJobReq);
        } catch (SchedulerException e) {
            LOG.error("手动执行任务失败", e);
            commonResp.setSuccess(false);
            commonResp.setMessage("执行失败: 调度异常");
        }
        return commonResp;
    }

    @RequestMapping(value = "/add")
    public CommonResp<Object> add(@RequestBody CronJobReq cronJobReq) {
        LOG.info("创建定时任务开始：{}", cronJobReq);
        CommonResp<Object> commonResp = new CommonResp<>();
        try {
            cronJobService.addJob(cronJobReq);
        } catch (SchedulerException e) {
            LOG.error("创建定时任务失败", e);
            commonResp.setSuccess(false);
            commonResp.setMessage("创建失败: 调度异常");
        } catch (ClassNotFoundException e) {
            LOG.error("创建定时任务失败：找不到类", e);
            commonResp.setSuccess(false);
            commonResp.setMessage("创建失败: 任务类不存在");
        }
        LOG.info("创建定时任务结束");
        return commonResp;
    }

    @RequestMapping(value = "/pause")
    public CommonResp<Object> pause(@RequestBody CronJobReq cronJobReq) {
        LOG.info("暂停定时任务开始：{}, {}", cronJobReq.getName(), cronJobReq.getGroup());
        CommonResp<Object> commonResp = new CommonResp<>();
        try {
            cronJobService.pauseJob(cronJobReq);
        } catch (SchedulerException e) {
            LOG.error("暂停定时任务失败", e);
            commonResp.setSuccess(false);
            commonResp.setMessage("暂停失败: 调度异常");
        }
        return commonResp;
    }

    @RequestMapping(value = "/resume")
    public CommonResp<Object> resume(@RequestBody CronJobReq cronJobReq) {
        LOG.info("重启定时任务开始：{}, {}", cronJobReq.getName(), cronJobReq.getGroup());
        CommonResp<Object> commonResp = new CommonResp<>();
        try {
            cronJobService.resumeJob(cronJobReq);
        } catch (SchedulerException e) {
            LOG.error("重启定时任务失败", e);
            commonResp.setSuccess(false);
            commonResp.setMessage("重启失败: 调度异常");
        }
        return commonResp;
    }

    @RequestMapping(value = "/reschedule")
    public CommonResp<Object> reschedule(@RequestBody CronJobReq cronJobReq) {
        LOG.info("更新定时任务开始：{}", cronJobReq);
        CommonResp<Object> commonResp = new CommonResp<>();
        try {
            cronJobService.rescheduleJob(cronJobReq);
        } catch (Exception e) {
            LOG.error("更新定时任务失败", e);
            commonResp.setSuccess(false);
            commonResp.setMessage("更新失败: 调度异常");
        }
        return commonResp;
    }

    @RequestMapping(value = "/delete")
    public CommonResp<Object> delete(@RequestBody CronJobReq cronJobReq) {
        LOG.info("删除定时任务开始：{}, {}", cronJobReq.getName(), cronJobReq.getGroup());
        CommonResp<Object> commonResp = new CommonResp<>();
        try {
            cronJobService.deleteJob(cronJobReq);
        } catch (SchedulerException e) {
            LOG.error("删除定时任务失败", e);
            commonResp.setSuccess(false);
            commonResp.setMessage("删除失败: 调度异常");
        }
        return commonResp;
    }

    @RequestMapping(value = "/query")
    public CommonResp<List<CronJobResp>> query() {
        LOG.info("查看所有定时任务开始");
        CommonResp<List<CronJobResp>> commonResp = new CommonResp<>();
        try {
            List<CronJobResp> list = cronJobService.queryJobs();
            commonResp.setContent(list);
        } catch (SchedulerException e) {
            LOG.error("查看定时任务失败", e);
            commonResp.setSuccess(false);
            commonResp.setMessage("查询失败: 调度异常");
        }
        LOG.info("查看定时任务结束");
        return commonResp;
    }
}