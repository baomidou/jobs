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

    int update(String registryGroup, String registryKey, String registryValue);

    int save(String registryGroup, String registryKey, String registryValue);

    int remove(String registryGroup, String registryKey, String registryValue);

    /**
     * 超时数据列表
     *
     * @param timeout 超时时长
     * @return
     */
    List<JobsRegistry> listTimeout(int timeout);
}
