package com.baomidou.jobs.starter.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
	private Integer id;
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
	 * 执行器路由策略
	 */
	private String routeStrategy;
	/**
	 * 阻塞处理策略
	 */
	private String blockStrategy;
	/**
	 * 任务执行超时时间，单位秒
	 */
	private Integer timeout;
	/**
	 * 失败重试次数
	 */
	private Integer failRetryCount;
	/**
	 * GLUE 类型	#GlueTypeEnum
	 */
	private String glueType;
	/**
	 * GLUE 源代码
	 */
	private String glueSource;
	/**
	 * GLUE 备注
	 */
	private String glueRemark;
	/**
	 * GLUE 更新时间
	 */
	private Date glueTime;
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
	 * 报警邮件
	 */
	private String alarmEmail;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态：0、运行 1、停止
	 */
	private Integer status;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
}
