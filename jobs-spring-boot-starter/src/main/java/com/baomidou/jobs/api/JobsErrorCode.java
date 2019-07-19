package com.baomidou.jobs.api;

import com.baomidou.jobs.JobsConstant;

/**
 * REST API 错误码
 *
 * @author jobob
 * @since 2019-06-08
 */
public enum JobsErrorCode implements IJobsErrorCode {
    /**
     * 失败
     */
    FAILED(JobsConstant.CODE_FAILED, "操作失败"),
    /**
     * 成功
     */
    SUCCESS(JobsConstant.CODE_SUCCESS, "执行成功");

    private final int code;
    private final String msg;

    JobsErrorCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static JobsErrorCode fromCode(int code) {
        JobsErrorCode[] ecs = JobsErrorCode.values();
        for (JobsErrorCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", code, msg);
    }
}
