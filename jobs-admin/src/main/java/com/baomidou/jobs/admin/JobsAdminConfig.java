package com.baomidou.jobs.admin;

import com.baomidou.jobs.starter.handler.IJobsAlarmHandler;
import com.baomidou.jobs.starter.handler.JobsAlarmSimpleHandler;
import com.baomidou.jobs.starter.starter.EnableJobsAdmin;
import com.baomidou.jobs.starter.starter.JobsProperties;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Jobs Admin 启动配置
 *
 * @author jobob
 * @since 2019-06-08
 */
@EnableJobsAdmin
@Configuration
@EnableConfigurationProperties(JobsProperties.class)
@MapperScan("com.baomidou.jobs.admin.mapper*")
@ComponentScan(basePackages = {
        "com.baomidou.jobs.admin.mapper",
        "com.baomidou.jobs.admin.service",
        "com.baomidou.jobs.admin.controller"
})
public class JobsAdminConfig {

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

}
