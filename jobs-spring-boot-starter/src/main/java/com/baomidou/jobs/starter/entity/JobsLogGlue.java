package com.baomidou.jobs.starter.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 任务日志表
 *
 * @author 青苗
 * @since 2019-05-30
 */
@Data
@Accessors(chain = true)
public class JobsLogGlue implements Serializable {
	private Integer id;
	/**
	 * 任务主键ID
	 */
	private Integer jobId;
	/**
	 * GLUE类型	#GlueTypeEnum
	 */
	private String glueType;
	private String glueSource;
	private String glueRemark;
	private String addTime;
	private String updateTime;

}
