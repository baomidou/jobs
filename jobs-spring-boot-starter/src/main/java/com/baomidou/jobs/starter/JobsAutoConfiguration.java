package com.baomidou.jobs.starter;

import com.baomidou.jobs.executor.JobsSpringExecutor;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.baomidou.jobs.rpc.serialize.impl.HessianSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

    @Bean
    @ConditionalOnMissingBean
    public IJobsRpcSerializer jobsRpcSerializer() {
        return new HessianSerializer();
    }

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public JobsSpringExecutor jobsSpringExecutor(JobsProperties jobsProperties) {
        JobsSpringExecutor jobsSpringExecutor = new JobsSpringExecutor();
        jobsSpringExecutor.setAccessToken(jobsProperties.getAdminAccessToken());
        jobsSpringExecutor.setAdminAddress(jobsProperties.getAdminAddress());
        jobsSpringExecutor.setApp(jobsProperties.getAppName());
        jobsSpringExecutor.setIp(jobsProperties.getAppIp());
        jobsSpringExecutor.setPort(jobsProperties.getAppPort());
        return jobsSpringExecutor;
    }
}
