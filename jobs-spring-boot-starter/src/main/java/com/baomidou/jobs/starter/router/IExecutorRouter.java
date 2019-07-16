package com.baomidou.jobs.starter.router;

import com.baomidou.jobs.starter.model.param.TriggerParam;
import com.baomidou.jobs.starter.api.JobsResponse;

import java.util.List;

/**
 * 执行器路由接口
 *
 * @author xxl jobob
 * @since 2019-06-27
 */
public interface IExecutorRouter {

    /**
     * 路由方法
     *
     * @param triggerParam 触发参数
     * @param addressList  待调用地址列表
     * @return
     */
    JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList);
}
