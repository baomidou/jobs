package com.baomidou.jobs.starter.starter;

import com.baomidou.jobs.starter.JobsHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动 Redis 分布式锁
 *
 * @author hubin
 * @since 2019-07-23
 */
@Configuration
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({JobsHelper.class, JobsScheduler.class, JobsMybatisPlusAutoConfiguration.class})
public @interface EnableJobsMybatisPlus {

}
