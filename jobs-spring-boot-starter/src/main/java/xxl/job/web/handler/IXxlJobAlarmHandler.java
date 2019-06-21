package xxl.job.web.handler;

import xxl.job.web.entity.XxlJobInfo;


/**
 * Job 报警处理器
 *
 * @author 青苗
 * @since 2019-06-08
 */
public interface IXxlJobAlarmHandler {

  /**
   * 调度失败
   *
   * @param jobInfo 任务信息
   * @return
   */
  boolean failed(XxlJobInfo jobInfo);
}
