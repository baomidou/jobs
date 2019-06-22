package com.baomidou.jobs.executor.service.jobhandler;

import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.handler.annotation.JobsHandler;
import com.baomidou.jobs.core.log.JobsLogger;
import com.baomidou.jobs.core.web.JobsResponse;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤：
 * 1、继承"IJobsHandler"：“com.baomidou.jobs.core.handler.IJobsHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobsHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "JobsLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobsHandler(value="demoJobHandler")
@Component
public class DemoJobHandler extends IJobsHandler {

	@Override
	public JobsResponse<String> execute(String param) throws Exception {
		JobsLogger.log("jobs, Hello World.");

		for (int i = 0; i < 5; i++) {
			JobsLogger.log("beat at:" + i);
			TimeUnit.SECONDS.sleep(2);
		}
		return JobsResponse.ok();
	}
}
