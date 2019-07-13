package com.baomidou.jobs.core;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Jobs 时钟辅助类
 *
 * @author jobob
 * @since 2019-07-13
 */
public class JobsClock {

    public static Timestamp now() {
        return Timestamp.from(Instant.now());
    }
}
