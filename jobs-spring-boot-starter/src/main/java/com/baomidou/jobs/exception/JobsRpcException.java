package com.baomidou.jobs.exception;

/**
 * Jobs RPC 异常
 *
 * @author jobob
 * @since 2019-07-19
 */
public class JobsRpcException extends JobsException {

    public JobsRpcException(String msg) {
        super(msg);
    }

    public JobsRpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JobsRpcException(Throwable cause) {
        super(cause);
    }

}