package com.baomidou.jobs.starter.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理任务信息表
 *
 * @author xxl jobob
 * @since 2019-05-30
 */
@Data
@Accessors(chain = true)
public class JobsInfo implements Serializable {
	/**
	 * 主键ID
	 */
	private Integer id;
	/**
	 * 执行器主键ID
	 */
	private Integer jobGroup;
	/**
	 * 任务执行CRON表达式
	 */
	private String jobCron;
	private String jobDesc;
	
	private Date addTime;
	private Date updateTime;
	/**
	 * 负责人
	 */
	private String author;
	/**
	 * 报警邮件
	 */
	private String alarmEmail;
	/**
	 * 执行器路由策略
	 */
	private String executorRouteStrategy;
	/**
	 * 执行器，任务Handler名称
	 */
	private String executorHandler;
	/**
	 * 执行器，任务参数
	 */
	private String executorParam;
	/**
	 * 阻塞处理策略
	 */
	private String executorBlockStrategy;
	/**
	 * 任务执行超时时间，单位秒
	 */
	private Integer executorTimeout;
	/**
	 * 失败重试次数
	 */
	private Integer executorFailRetryCount;
	/**
	 * GLUE类型	#GlueTypeEnum
	 */
	private String glueType;
	/**
	 * GLUE源代码
	 */
	private String glueSource;
	/**
	 * GLUE备注
	 */
	private String glueRemark;
	/**
	 * GLUE更新时间
	 */
	private Date glueUpdatetime;
	/**
	 * 子任务ID，多个逗号分隔
	 */
	private String childJobid;
	/**
	 * 调度状态：0-停止，1-运行
	 */
	private Integer triggerStatus;
	/**
	 * 上次调度时间
	 */
	private Long triggerLastTime;
	/**
	 * 下次调度时间
	 */
	private Long triggerNextTime;

}
