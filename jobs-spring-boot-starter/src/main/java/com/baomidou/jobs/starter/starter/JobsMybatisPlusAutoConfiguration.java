package com.baomidou.jobs.starter.starter;

import com.baomidou.jobs.starter.handler.IJobsAlarmHandler;
import com.baomidou.jobs.starter.handler.JobsAlarmSimpleHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Job 启动参数
 *
 * @author xxl jobob
 * @since 2019-06-08
 */
@Configuration
@EnableConfigurationProperties(JobsProperties.class)
@MapperScan("com.baomidou.jobs.starter.mybatisplus.mapper*")
@ComponentScan(basePackages = {
        "com.baomidou.jobs.starter.mybatisplus.mapper",
        "com.baomidou.jobs.starter.mybatisplus.service",
        "com.baomidou.jobs.starter.controller"
})
public class JobsMybatisPlusAutoConfiguration {

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public IJobsAlarmHandler xxlJobAlarmHandler() {
        return new JobsAlarmSimpleHandler();
    }
//
//    @Bean
//    public XxlJobHelper XxlJobHelper() {
//        return new XxlJobHelper();
//    }
}
