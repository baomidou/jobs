package com.baomidou.jobs.core.web;

import com.baomidou.jobs.core.model.HandleCallbackParam;
import com.baomidou.jobs.core.model.RegistryParam;

import java.util.List;

/**
 * Jobs Admin
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
public interface IJobsAdmin {

    String MAPPING = "/api";


    // ---------------------- callback ----------------------

    /**
     * callback
     *
     * @param callbackParamList
     * @return
     */
    JobsResponse<String> callback(List<HandleCallbackParam> callbackParamList);

    /**
     * 客户端注册
     *
     * @param registryParam
     * @return
     */
    JobsResponse<String> registry(RegistryParam registryParam);

    /**
     * registry remove
     *
     * @param registryParam
     * @return
     */
    JobsResponse<String> registryRemove(RegistryParam registryParam);

}
