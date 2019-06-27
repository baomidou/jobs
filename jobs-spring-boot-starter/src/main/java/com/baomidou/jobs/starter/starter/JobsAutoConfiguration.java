package com.baomidou.jobs.starter.starter;

import com.baomidou.jobs.core.executor.JobsSpringExecutor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Job 启动配置
 *
 * @author jobob
 * @since 2019-06-08
 */
@Configuration
@EnableConfigurationProperties(JobsProperties.class)
public class JobsAutoConfiguration {

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public JobsSpringExecutor xxlJobExecutor(JobsProperties jobsProperties) {
        JobsSpringExecutor jobsSpringExecutor = new JobsSpringExecutor();
        jobsSpringExecutor.setAccessToken(jobsProperties.getAccessToken());
        jobsSpringExecutor.setAdminAddress(jobsProperties.getAdminAddress());
        JobsProperties.App app = jobsProperties.getApp();
        jobsSpringExecutor.setApp(app.getName());
        jobsSpringExecutor.setIp(app.getIp());
        jobsSpringExecutor.setPort(app.getPort());
        return jobsSpringExecutor;
    }
}
