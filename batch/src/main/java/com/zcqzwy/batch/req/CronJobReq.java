package com.zcqzwy.batch.req;

/**
 * @author 29282
 * 一个 DTO（数据传输对象），通常用来作为 Web 接口接收前端请求参数的载体
 * 你可以通过调用 API 接口，在网页上“动态地”添加或修改一个基于 Cron 表达式的定时任务。
 * CronJobReq 就是一个“任务指令单”。前端把老板（用户）的要求填在这个单子上交给后端，
 * 后端照着单子上的信息（名字、分组、执行时间），去配置 Quartz 调度器，从而实现定时任务的动态增删改查。
 */
public class CronJobReq {

    /**
     * 对应 Quartz：JobKey.group 或 TriggerKey.group。
     * 作用：为了方便管理。比如你可以把财务相关的任务设为 finance_group，报表相关的设为 report_group。
     * 如果前端不传，通常后台会给一个默认值（如 DEFAULT）
     */
    private String group;

    /**
     * 对应 Quartz：JobKey.name 或 TriggerKey.name。
     * 作用：任务的唯一标识 ID。在同一个 group 下，name 必须是唯一的。
     * 通过 group + name 就能在数据库或内存中精准定位到一个具体的定时任务。
     */
    private String name;

    /**
     * 对应 Quartz：JobDetail.description。
     * 作用：给人类看的备注。
     * 比如“每天凌晨2点同步用户数据”、“每5分钟检查一次订单超时”。通常用于前端页面的列表展示。
     */
    private String description;

    /**
     * 对应 Quartz：CronTrigger。
     * 作用：这是最核心的字段，决定了这个任务什么时候执行。
     * 比如前端传过来一个 0 0 12 * * ?，就代表每天中午 12 点触发。
     */
    private String cronExpression;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CronJobDto{");
        sb.append("cronExpression='").append(cronExpression).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
