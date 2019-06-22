package com.baomidou.jobs.core.model;

import com.baomidou.jobs.core.web.JobsResponse;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by xuxueli on 17/3/2.
 */
@Data
@ToString
public class HandleCallbackParam implements Serializable {
    private int logId;
    private long logDateTim;

    private JobsResponse<String> executeResult;

    public HandleCallbackParam() {
        // to do nothing
    }

    public HandleCallbackParam(int logId, long logDateTim, JobsResponse<String> executeResult) {
        this.logId = logId;
        this.logDateTim = logDateTim;
        this.executeResult = executeResult;
    }
}
