package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.starter.model.param.HandleCallbackParam;
import com.baomidou.jobs.starter.model.param.RegistryParam;
import com.baomidou.jobs.starter.api.JobsResponse;

import java.util.List;

/**
 * Jobs Admin
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
public interface IJobsAdminService {

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
