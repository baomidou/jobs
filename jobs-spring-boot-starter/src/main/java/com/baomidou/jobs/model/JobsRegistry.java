package com.baomidou.jobs.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
     * 主键ID
     */
    private Long id;
    /**
     * 服务名
     */
    private String app;
    /**
     * 地址 = IP:PORT 例如：127.0.0.1:9999
     */
    private String address;
    /**
     * 状态：0、启用 1、已禁用
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Long updateTime;

}
