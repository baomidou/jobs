package com.baomidou.jobs.sample;

import com.baomidou.jobs.core.executor.JobsSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jobs config
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
@Slf4j
@Configuration
public class JobsConfig {
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean(initMethod = "start", destroyMethod = "destroy")
    public JobsSpringExecutor xxlJobExecutor() {
        log.info("Jobs config init.");
        JobsSpringExecutor jobsSpringExecutor = new JobsSpringExecutor();
        jobsSpringExecutor.setAdminAddresses(adminAddresses);
        jobsSpringExecutor.setAppName(appName);
        jobsSpringExecutor.setIp(ip);
        jobsSpringExecutor.setPort(port);
        jobsSpringExecutor.setAccessToken(accessToken);
        jobsSpringExecutor.setLogPath(logPath);
        jobsSpringExecutor.setLogRetentionDays(logRetentionDays);
        return jobsSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */


}