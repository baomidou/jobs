package com.baomidou.jobs.starter.handler;

import com.baomidou.jobs.starter.entity.JobsInfo;


/**
 * Job 报警处理器
 *
 * @author xxl jobob
 * @since 2019-06-08
 */
public interface IJobsAlarmHandler {

  /**
   * 调度失败
   *
   * @param jobInfo 任务信息
   * @return
   */
  boolean failed(JobsInfo jobInfo);
}
