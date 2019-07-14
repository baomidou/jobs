package com.baomidou.jobs.core.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by xuxueli on 16/7/22.
 */
@Data
@ToString
public class TriggerParam implements Serializable{
    private int jobId;
    private String executorHandler;
    private String executorParams;
    private String executorBlockStrategy;
    private int executorTimeout;

    private int logId;
    private long logDateTim;

}
