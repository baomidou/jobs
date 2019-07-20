package com.baomidou.jobs.router;

import java.util.List;

/**
 * 执行器路由接口
 *
 * @author jobob
 * @since 2019-06-27
 */
public interface IJobsExecutorRouter {

    /**
     * 路由方法
     *
     * @param app         客户端名称
     * @param addressList 待调用地址列表
     * @return 路由选择执行地址
     */
    String route(String app, List<String> addressList);
}
