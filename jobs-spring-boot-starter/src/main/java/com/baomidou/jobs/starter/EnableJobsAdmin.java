package com.baomidou.jobs.starter;

import com.baomidou.jobs.disruptor.JobsDisruptorAutoConfiguration;
import com.baomidou.jobs.service.JobsHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动 Jobs Admin
 *
 * @author jobob
 * @since 2019-06-08
 */
@Configuration
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({JobsDisruptorAutoConfiguration.class, JobsHelper.class, JobsScheduler.class})
public @interface EnableJobsAdmin {

}
