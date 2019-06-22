package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.starter.entity.JobsGroup;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 任务组服务接口
 *
 * @author 青苗
 * @since 2019-06-15
 */
public interface IJobsGroupService<P> {

    /**
     * 分页
     *
     * @param request  当前请求
     * @param jobGroup 实体对象
     * @return
     */
    P page(HttpServletRequest request, JobsGroup jobGroup);

    /**
     * 查询所有任务组
     */
    List<JobsGroup> listAll();

    /**
     * 根据 addressType 查询任务组
     */
    List<JobsGroup> listByAddressType(int addressType);

    /**
     * 删除、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean remove(int id);

    JobsGroup getById(int id);

    boolean updateById(JobsGroup group);
}
