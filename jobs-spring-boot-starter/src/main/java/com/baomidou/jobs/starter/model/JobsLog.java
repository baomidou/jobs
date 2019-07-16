package com.baomidou.jobs.starter.model;

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
	 * 执行器
	 */
	private String executorAddress;
	private String executorHandler;
	private String executorParam;
	private Integer executorFailRetryCount;

	/**
	 * 触发器
	 */
	private Integer triggerCode;
	private String triggerMsg;
	private Long triggerTime;

	/**
	 * 处理器
	 */
	private Date handleTime;
	private Integer handleCode;
	private String handleMsg;

	/**
	 * 创建时间
	 */
	private Long createTime;

}
