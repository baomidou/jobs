package com.baomidou.jobs.starter.web;

import com.baomidou.jobs.starter.model.HandleCallbackParam;
import com.baomidou.jobs.starter.model.RegistryParam;

import java.util.List;

/**
 * Jobs Admin
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
public interface IJobsAdmin {

    /**
     * callback
     *
     * @param callbackParamList
     * @return
     */
    JobsResponse<Boolean> callback(List<HandleCallbackParam> callbackParamList);

    /**
     * 客户端注册
     *
     * @param registryParam
     * @return
     */
    JobsResponse<Boolean> registry(RegistryParam registryParam);

    /**
     * registry remove
     *
     * @param registryParam
     * @return
     */
    JobsResponse<Boolean> registryRemove(RegistryParam registryParam);

}
