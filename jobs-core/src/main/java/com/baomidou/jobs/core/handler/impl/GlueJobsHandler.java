package com.baomidou.jobs.core.handler.impl;

import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.web.JobsResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * glue job handler
 *
 * @author xuxueli 2016-5-19 21:05:45
 */
@Slf4j
public class GlueJobsHandler implements IJobsHandler {

    private long glueUpdatetime;
    private IJobsHandler jobHandler;

    public GlueJobsHandler(IJobsHandler jobHandler, long glueUpdatetime) {
        this.jobHandler = jobHandler;
        this.glueUpdatetime = glueUpdatetime;
    }

    public long getGlueUpdatetime() {
        return glueUpdatetime;
    }

    @Override
    public JobsResponse<String> execute(String param) throws Exception {
        log.debug("glue.version:{}", glueUpdatetime);
        return jobHandler.execute(param);
    }
}
