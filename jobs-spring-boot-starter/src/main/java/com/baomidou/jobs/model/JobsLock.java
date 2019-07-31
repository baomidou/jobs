package com.baomidou.jobs.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 管理任务日志表
 *
 * @author jobob
 * @since 2019-07-13
 */
@Data
@ToString
@Accessors(chain = true)
public class JobsLock implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 持有者
     */
    private String owner;
    /**
     * 创建时间
     */
    private Long createTime;

}
