package com.baomidou.jobs.rpc.registry.client.model;

import lombok.Data;

import java.util.List;

/**
 * @author xuxueli 2018-12-03
 */
@Data
public class JobsRegistryParamVO {
    private String accessToken;
    private String biz;
    private String env;
    private List<JobsRegistryDataParamVO> registryDataList;
    private List<String> keys;

}
