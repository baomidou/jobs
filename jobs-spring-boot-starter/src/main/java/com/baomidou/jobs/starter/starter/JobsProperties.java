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
    /**
     * Jobs admin address, such as "http://address" or "http://address01,http://address02"
     */
    private String adminAddress;
    /**
     * 客户端相关配置
     */
    private App app;

    /**
     * 客户端参数
     */
    @Data
    public static class App {
        /**
         * APP 服务名
         */
        private String name;
        /**
         * IP 地址
         */
        private String ip;
        /**
         * 端口
         */
        private int port;
    }
}
