package com.baomidou.jobs.starter;

import com.baomidou.jobs.executor.JobsSpringExecutor;
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
        jobsSpringExecutor.setAccessToken(jobsProperties.getAdminAccessToken());
        jobsSpringExecutor.setAdminAddress(jobsProperties.getAdminAddress());
        jobsSpringExecutor.setApp(jobsProperties.getAppName());
        jobsSpringExecutor.setIp(jobsProperties.getAppIp());
        jobsSpringExecutor.setPort(jobsProperties.getAppPort());
        return jobsSpringExecutor;
    }
}
