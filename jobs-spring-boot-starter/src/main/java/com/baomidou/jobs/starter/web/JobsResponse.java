package com.baomidou.jobs.starter.web;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Optional;

/**
 * REST API 返回结果
 *
 * @author xxl jobob
 * @since 2019-06-08
 */
@Data
@ToString
@Accessors(chain = true)
public class JobsResponse<T> implements Serializable {
    /**
     * 业务错误码
     */
    private int code;
    /**
     * 结果集
     */
    private T data;
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
        return ok("ok");
    }

    public static <T> JobsResponse<T> ok(T data) {
        JobsErrorCode aec = JobsErrorCode.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            aec = JobsErrorCode.FAILED;
        }
        return restResult(data, aec);
    }

    public static <T> JobsResponse<T> failed(String msg) {
        return restResult(null, JobsErrorCode.FAILED.getCode(), msg);
    }

    public static <T> JobsResponse<T> failed(IJobsErrorCode errorCode) {
        return restResult(null, errorCode);
    }

    public static <T> JobsResponse<T> restResult(T data, IJobsErrorCode errorCode) {
        return restResult(data, errorCode.getCode(), errorCode.getMsg());
    }

    private static <T> JobsResponse<T> restResult(T data, int code, String msg) {
        JobsResponse<T> apiResult = new JobsResponse<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}