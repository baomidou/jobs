package com.baomidou.jobs.admin.service;

import java.util.List;

/**
 * 任务启动节点注册接口
 *
 * @author jobob
 * @since 2019-07-18
 */
public interface IJobsRegistryService {

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

    int update(String app, String address, int status);

    int save(String app, String address, int status);

    /**
     * 在线可用机器数
     */
    int countOnline();

    /**
     * 任务数量
     */
    int countAll();
}
