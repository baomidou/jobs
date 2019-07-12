package com.baomidou.jobs.starter.entity;

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
	private Integer id;

	/**
	 * job info
	 */
	private Integer jobGroup;
	private Integer jobId;

	/**
	 * execute info
	 */
	private String executorAddress;
	private String executorHandler;
	private String executorParam;
//	private String executorShardingParam;
	private Integer executorFailRetryCount;

	/**
	 * trigger info
	 */
	private Date triggerTime;
	private Integer triggerCode;
	private String triggerMsg;

	/**
	 * handle info
	 */
	private Date handleTime;
	private Integer handleCode;
	private String handleMsg;

	/**
	 * alarm info
	 */
	private Integer alarmStatus;

}
