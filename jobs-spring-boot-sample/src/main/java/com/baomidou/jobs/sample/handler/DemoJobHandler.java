package com.baomidou.jobs.sample.handler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
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
    public JobsResponse execute(String tenantId, String param) throws JobsException {
        System.out.println("执行 DemoJobHandler tenantId=" + tenantId
            + ",param=" + param);
        return JobsResponse.ok();
    }
}
