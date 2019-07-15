package com.baomidou.jobs.sample.handler;

import com.baomidou.jobs.starter.handler.IJobsHandler;
import com.baomidou.jobs.starter.web.JobsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * 任务Handler示例（Bean模式）
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
@Slf4j
@Component
public class DemoJobHandler implements IJobsHandler {

	@Override
	public JobsResponse<String> execute(String param) throws Exception {
		System.out.println("执行 DemoJobHandler");
		log.info("jobs, Hello World.");

		for (int i = 0; i < 5; i++) {
			log.info("beat at:" + i);
			TimeUnit.SECONDS.sleep(2);
		}
		return JobsResponse.ok();
	}
}
