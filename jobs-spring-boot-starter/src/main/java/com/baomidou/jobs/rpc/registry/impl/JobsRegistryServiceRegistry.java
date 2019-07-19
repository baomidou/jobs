package com.baomidou.jobs.rpc.registry.impl;

import com.baomidou.jobs.rpc.registry.ServiceRegistry;
import com.baomidou.jobs.rpc.registry.client.JobsRegistryClient;
import com.baomidou.jobs.rpc.registry.client.model.JobsRegistryDataParamVO;

import java.util.*;

/**
 * service registry for "xxl-registry v1.0.1"
 *
 * @author xuxueli 2018-11-30
 */
public class JobsRegistryServiceRegistry extends ServiceRegistry {

    public static final String XXL_REGISTRY_ADDRESS = "XXL_REGISTRY_ADDRESS";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String BIZ = "BIZ";
    public static final String ENV = "ENV";

    private JobsRegistryClient xxlRegistryClient;
    public JobsRegistryClient getXxlRegistryClient() {
        return xxlRegistryClient;
    }

    @Override
    public void start(Map<String, String> param) {
        String xxlRegistryAddress = param.get(XXL_REGISTRY_ADDRESS);
        String accessToken = param.get(ACCESS_TOKEN);
        String biz = param.get(BIZ);
        String env = param.get(ENV);

        // fill
        biz = (biz!=null&&biz.trim().length()>0)?biz:"default";
        env = (env!=null&&env.trim().length()>0)?env:"default";

        xxlRegistryClient = new JobsRegistryClient(xxlRegistryAddress, accessToken, biz, env);
    }

    @Override
    public void stop() {
        if (xxlRegistryClient != null) {
            xxlRegistryClient.stop();
        }
    }

    @Override
    public boolean registry(Set<String> keys, String value) {
        if (keys==null || keys.size() == 0 || value == null) {
            return false;
        }

        // init
        List<JobsRegistryDataParamVO> registryDataList = new ArrayList<>();
        for (String key:keys) {
            registryDataList.add(new JobsRegistryDataParamVO(key, value));
        }

        return xxlRegistryClient.registry(registryDataList);
    }

    @Override
    public boolean remove(Set<String> keys, String value) {
        if (keys==null || keys.size() == 0 || value == null) {
            return false;
        }

        // init
        List<JobsRegistryDataParamVO> registryDataList = new ArrayList<>();
        for (String key:keys) {
            registryDataList.add(new JobsRegistryDataParamVO(key, value));
        }

        return xxlRegistryClient.remove(registryDataList);
    }

    @Override
    public Map<String, TreeSet<String>> discovery(Set<String> keys) {
        return xxlRegistryClient.discovery(keys);
    }

    @Override
    public TreeSet<String> discovery(String key) {
        return xxlRegistryClient.discovery(key);
    }

}
