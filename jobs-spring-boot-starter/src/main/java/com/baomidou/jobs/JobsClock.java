package com.baomidou.jobs;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

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

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static Date date(long currentTimeMillis) {
        return new Date(currentTimeMillis);
    }
}
