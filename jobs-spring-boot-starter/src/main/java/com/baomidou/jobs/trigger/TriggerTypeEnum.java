package com.baomidou.jobs.trigger;

/**
 * trigger type enum
 *
 * @author xuxueli 2018-09-16 04:56:41
 */
public enum TriggerTypeEnum {
    MANUAL("手动触发"),
    CRON("Cron触发"),
    RETRY("失败重试触发"),
    PARENT("父任务触发"),
    API("API触发");

    TriggerTypeEnum(String title){
        this.title = title;
    }

    private String title;

    public String getTitle() {
        return title;
    }

}
