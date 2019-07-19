package com.baomidou.jobs;

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
    /**
     * 清理时长，需比心跳稍大
     */
    int CLEAN_TIMEOUT = 50000;
    String COMMA = ",";
    /**
     * API URI
     */
    String JOBS_API = "/jobs-api";
    /**
     * owner标志常量，用于标志是否做过tryLock()操作
     */
    String OPERATION_TRY_LOCK = "OPERATION_TRY_LOCK";
    /**
     * 锁唯一标示
     */
    String DEFAULT_LOCK_KEY = "JOBS_LOCK";

}
