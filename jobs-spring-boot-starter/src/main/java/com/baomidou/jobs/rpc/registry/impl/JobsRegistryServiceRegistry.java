package com.baomidou.jobs.rpc.registry.impl;

import com.baomidou.jobs.rpc.registry.IJobsServiceRegistry;
import com.baomidou.jobs.rpc.registry.client.JobsRegistryClient;
import com.baomidou.jobs.rpc.registry.client.model.JobsRegistryDataParamVO;

import java.util.*;

/**
 * service registry for "jobs-registry v1.0.1"
 *
 * @author xuxueli 2018-11-30
 */
public class JobsRegistryServiceRegistry implements IJobsServiceRegistry {

    public static final String REGISTRY_ADDRESS = "REGISTRY_ADDRESS";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String BIZ = "BIZ";
    public static final String ENV = "ENV";

    private JobsRegistryClient jobsRegistryClient;

    public JobsRegistryClient getJobsRegistryClient() {
        return jobsRegistryClient;
    }

    @Override
    public void start(Map<String, String> param) {
        String xxlRegistryAddress = param.get(REGISTRY_ADDRESS);
        String accessToken = param.get(ACCESS_TOKEN);
        String biz = param.get(BIZ);
        String env = param.get(ENV);

        // fill
        biz = (biz != null && biz.trim().length() > 0) ? biz : "default";
        env = (env != null && env.trim().length() > 0) ? env : "default";

        jobsRegistryClient = new JobsRegistryClient(xxlRegistryAddress, accessToken, biz, env);
    }

    @Override
    public void stop() {
        if (jobsRegistryClient != null) {
            jobsRegistryClient.stop();
        }
    }

    @Override
    public boolean registry(Set<String> keys, String value) {
        if (keys == null || keys.size() == 0 || value == null) {
            return false;
        }

        // init
        List<JobsRegistryDataParamVO> registryDataList = new ArrayList<>();
        for (String key : keys) {
            registryDataList.add(new JobsRegistryDataParamVO(key, value));
        }

        return jobsRegistryClient.registry(registryDataList);
    }

    @Override
    public boolean remove(Set<String> keys, String value) {
        if (keys == null || keys.size() == 0 || value == null) {
            return false;
        }

        // init
        List<JobsRegistryDataParamVO> registryDataList = new ArrayList<>();
        for (String key : keys) {
            registryDataList.add(new JobsRegistryDataParamVO(key, value));
        }

        return jobsRegistryClient.remove(registryDataList);
    }

    @Override
    public Map<String, TreeSet<String>> discovery(Set<String> keys) {
        return jobsRegistryClient.discovery(keys);
    }

    @Override
    public TreeSet<String> discovery(String key) {
        return jobsRegistryClient.discovery(key);
    }

}
