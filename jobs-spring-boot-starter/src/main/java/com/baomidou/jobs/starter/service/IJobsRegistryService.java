package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.starter.JobsConstant;

import java.util.List;

public interface IJobsRegistryService {

    /**
     * 清理超时节点
     */
    default int cleanTimeout() {
        return removeTimeOut(JobsConstant.CLEAN_TIMEOUT);
    }

    /**
     * 删除超时数据
     *
     * @param timeout 超时时长
     * @return
     */
    int removeTimeOut(int timeout);

    /**
     * 查询注册地址列表
     *
     * @param app 客户端 APP 名称
     * @return
     */
    List<String> listAddress(String app);

    int update(String app, String address);

    int save(String app, String address);

    int remove(String app, String address);
}
