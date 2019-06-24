package com.baomidou.jobs.starter.router;

import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.web.JobsResponse;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public abstract class ExecutorRouter {

    /**
     * router address
     *
     * @param addressList
     * @return  JobsResponse.content=address
     */
    public abstract JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList);
}
