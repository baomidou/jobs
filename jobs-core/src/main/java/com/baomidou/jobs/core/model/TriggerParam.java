package com.baomidou.jobs.core.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 触发器参数
 *
 * @author jobob
 * @since 2019-07-15
 */
@Data
@ToString
public class TriggerParam implements Serializable{
    private Long jobId;
    private String executorHandler;
    private String executorParams;
    private String executorBlockStrategy;
    private int executorTimeout;

    private Long logId;
    private Long logDateTime;

}
