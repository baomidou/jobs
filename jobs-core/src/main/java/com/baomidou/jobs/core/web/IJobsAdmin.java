package com.baomidou.jobs.core.web;

import com.baomidou.jobs.core.model.HandleCallbackParam;
import com.baomidou.jobs.core.model.RegistryParam;

import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:52:49
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


    // ---------------------- registry ----------------------

    /**
     * registry
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
