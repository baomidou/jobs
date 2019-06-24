package com.baomidou.jobs.starter.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 管理任务组表
 *
 * @author xxl jobob
 * @since 2019-05-30
 */
@Data
@Accessors(chain = true)
public class JobsGroup implements Serializable {
    private Integer id;
    private String app;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 执行器地址类型：0=自动注册、1=手动录入
     */
    private Integer type;
    /**
     * 执行器地址列表，多地址逗号分隔(手动录入)
     */
    private String address;

}
