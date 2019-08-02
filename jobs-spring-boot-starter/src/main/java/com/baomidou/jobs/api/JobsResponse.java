package com.baomidou.jobs.api;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Optional;

/**
 * REST API 返回结果
 *
 * @author jobob
 * @since 2019-06-08
 */
@Data
@ToString
@Accessors(chain = true)
public class JobsResponse implements Serializable {
    /**
     * 业务错误码
     */
    private int code;
    /**
     * 描述
     */
    private String msg;

    public JobsResponse() {
        // to do nothing
    }

    public JobsResponse(IJobsErrorCode errorCode) {
        errorCode = Optional.ofNullable(errorCode).orElse(JobsErrorCode.FAILED);
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public static JobsResponse ok() {
        return restResult(JobsErrorCode.SUCCESS);
    }

    public static JobsResponse failed(String msg) {
        return restResult(JobsErrorCode.FAILED.getCode(), msg);
    }

    public static JobsResponse restResult(IJobsErrorCode errorCode) {
        return restResult(errorCode.getCode(), errorCode.getMsg());
    }

    private static JobsResponse restResult(int code, String msg) {
        JobsResponse apiResult = new JobsResponse();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        return apiResult;
    }
}