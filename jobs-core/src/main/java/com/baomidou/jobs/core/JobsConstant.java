package com.baomidou.jobs.core;

/**
 * 常量
 *
 * @author jobob
 * @since 2019-07-13
 */
public interface JobsConstant {
    int CODE_SUCCESS = 0;
    int CODE_FAILED = -1;
    /**
     * 心跳时长
     */
    int BEAT_TIMEOUT = 30;
    String COMMA = ",";
    /**
     * API URI
     */
    String JOBS_API = "/jobs-api";

}
