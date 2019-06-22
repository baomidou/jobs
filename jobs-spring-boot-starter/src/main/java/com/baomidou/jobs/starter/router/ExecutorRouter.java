package com.baomidou.jobs.starter.router;

import com.baomidou.jobs.core.biz.model.ReturnT;
import com.baomidou.jobs.core.biz.model.TriggerParam;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public abstract class ExecutorRouter {

    /**
     * router address
     *
     * @param addressList
     * @return  ReturnT.content=address
     */
    public abstract ReturnT<String> route(TriggerParam triggerParam, List<String> addressList);

}
