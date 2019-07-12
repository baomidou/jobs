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

    String MAPPING = "/job-api";

    /**
     * 数据库行锁 SQL
     *
     * @return
     */
    String lockSql();

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
