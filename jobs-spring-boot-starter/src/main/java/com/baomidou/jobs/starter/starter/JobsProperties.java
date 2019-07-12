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
     * admin 访问票据
     */
    private String adminAccessToken;
    /**
     * Jobs admin address, such as "http://address" or "http://address01,http://address02"
     */
    private String adminAddress;
    /**
     * APP 服务名
     */
    private String appName;
    /**
     * APP IP 地址
     */
    private String appIp;
    /**
     * APP 端口
     */
    private int appPort;
    /**
     * APP 访问票据
     */
    private String appAccessToken;

}
