package com.baomidou.jobs.disruptor;

import com.baomidou.jobs.model.JobsInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 日志事件
 *
 * @author jobob
 * @since 2019-06-27
 */
@Data
public class JobsInfoEvent implements Serializable {
    /**
     * 任务信息
     */
    private JobsInfo jobsInfo;
    /**
     * 等待执行时长
     */
    private long waitSecond;

}
