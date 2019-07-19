package com.baomidou.jobs.exception;

/**
 * Jobs 异常
 *
 * @author jobob
 * @since 2019-07-13
 */
public class JobsException extends RuntimeException {

    public JobsException() {
        // to do nothing
    }

    public JobsException(String message) {
        super(message);
    }

    public JobsException(Throwable cause) {
        super(cause);
    }

    public JobsException(String message, Throwable cause) {
        super(message, cause);
    }
}
