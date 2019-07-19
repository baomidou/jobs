package com.baomidou.jobs.sample.handler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.handler.IJobsHandler;
import org.springframework.stereotype.Component;


/**
 * 处理器 DEMO
 *
 * @author jobob
 * @since 2019-07-16
 */
@Component
public class DemoJobHandler implements IJobsHandler {

    @Override
    public JobsResponse<String> execute(String param) throws Exception {
        System.out.println("执行 DemoJobHandler");
        return JobsResponse.ok();
    }
}
