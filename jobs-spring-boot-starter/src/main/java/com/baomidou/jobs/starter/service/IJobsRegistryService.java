package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.starter.entity.JobsRegistry;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

public interface IJobsRegistryService<P extends Serializable> {

    /**
     * 分页
     *
     * @param request     当前请求
     * @param jobRegistry 实体对象
     * @return
     */
    P page(HttpServletRequest request, JobsRegistry jobRegistry);

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

    int update(String app, String ip, String port);

    int save(String app, String ip, String port);

    int remove(String app, String ip, String port);

    /**
     * 超时数据列表
     *
     * @param timeout 超时时长
     * @return
     */
    List<JobsRegistry> listTimeout(int timeout);
}
