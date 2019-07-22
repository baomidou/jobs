package com.baomidou.jobs.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理任务日志表
 *
 * @author xxl jobob
 * @since 2019-05-30
 */
@Data
@ToString
@Accessors(chain = true)
public class JobsLog implements Serializable {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 任务ID
	 */
	private Long jobId;
	/**
	 * 执行器地址
	 */
	private String address;
	/**
	 * 执行器，任务 Handler 名称
	 */
	private String handler;
	/**
	 * 执行器，任务参数
	 */
	private String param;
	/**
	 * 失败重试次数
	 */
	private Integer failRetryCount;
	/**
	 * 触发器调度返回码
	 */
	private Integer triggerCode;
	/**
	 * 触发器调度类型
	 */
	private String triggerType;
	/**
	 * 触发器调度返回信息
	 */
	private String triggerMsg;
	/**
	 * 创建时间
	 */
	private Long createTime;

}
