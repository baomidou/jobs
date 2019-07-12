package com.baomidou.jobs.starter.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务注册表
 *
 * @author xxl jobob
 * @since 2019-05-30
 */
@Data
@ToString
@Accessors(chain = true)
public class JobsRegistry implements Serializable {
    /**
     * 主键 ID
     */
    private Integer id;
    /**
     * 服务名
     */
    private String app;
    /**
     * IP 地址
     */
    private String ip;
    /**
     * 端口
     */
    private String port;
    /**
     * 更新时间
     */
    private Date updateTime;

}
