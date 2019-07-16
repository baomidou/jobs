package com.baomidou.jobs.admin.service.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 重要数量统计 VO
 *
 * @author xxl jobob
 * @since 2019-06-15
 */
@Data
@Accessors(chain = true)
public class JobsImportantNumVO {
    /**
     * 调度中心运行的任务数量
     */
    private Integer runTaskNum;
    /**
     * 调度中心触发的调度次数
     */
    private Integer triggeredNum;
    /**
     * 调度中心在线的执行器机器数量
     */
    private Integer onlineExecutorNum;

}
