package com.baomidou.jobs.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 管理任务信息表
 *
 * @author jobob
 * @since 2019-07-12
 */
@Data
@ToString
@Accessors(chain = true)
public class JobsInfo implements Serializable {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 租户ID
	 */
	private String tenantId;
	/**
	 * 对应 JobsRegistry app 属性
	 */
	private String app;
	/**
	 * 任务执行CRON表达式
	 */
	private String cron;
	/**
	 * 执行器，任务 Handler 名称
	 */
	private String handler;
	/**
	 * 执行器，任务参数
	 */
	private String param;
	/**
	 * 任务执行超时时间，单位秒
	 */
	private Integer timeout;
	/**
	 * 失败重试次数
	 */
	private Integer failRetryCount;
	/**
	 * 上次调度时间
	 */
	private Long lastTime;
	/**
	 * 下次调度时间
	 */
	private Long nextTime;
	/**
	 * 负责人
	 */
	private String author;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态：0、启用 1、已禁用
	 */
	private Integer status;
	/**
	 * 更新时间
	 */
	private Long updateTime;
	/**
	 * 创建时间
	 */
	private Long createTime;
}
