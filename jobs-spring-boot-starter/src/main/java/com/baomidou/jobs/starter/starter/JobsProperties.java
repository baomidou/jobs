package com.baomidou.jobs.starter.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Job 启动参数
 *
 * @author xxl jobob
 * @since 2019-06-08
 */
@Data
@ConfigurationProperties(JobsProperties.PREFIX)
public class JobsProperties {
    public static final String PREFIX = "jobs";
    /**
     * 访问票据
     */
    private String accessToken;

}
