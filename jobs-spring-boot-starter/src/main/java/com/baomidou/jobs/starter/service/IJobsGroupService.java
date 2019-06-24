package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.core.JobsConstant;
import com.baomidou.jobs.core.model.RegistryParam;
import com.baomidou.jobs.starter.entity.JobsGroup;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 任务组服务接口
 *
 * @author xxl jobob
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

    boolean save(JobsGroup jobsGroup);

    JobsGroup getById(int id);

    JobsGroup getByApp(String appName);

    boolean updateById(JobsGroup group);

    default boolean registry(RegistryParam registryParam) {
        if (!StringUtils.isEmpty(registryParam.getRegistryKey())
                && !StringUtils.isEmpty(registryParam.getRegistryValue())) {
            JobsGroup jobsGroup = getByApp(registryParam.getRegistryKey());
            if (null == jobsGroup) {
                // 不存在新增
                jobsGroup = new JobsGroup();
                jobsGroup.setApp(registryParam.getRegistryKey());
                jobsGroup.setType(0);
                jobsGroup.setAddress(registryParam.getRegistryValue());
                return save(jobsGroup);
            }
            if (jobsGroup.getAddress().contains(registryParam.getRegistryValue())) {
                // 存在直接返回
                return true;
            }
            // 新增节点
            JobsGroup temp = new JobsGroup();
            temp.setId(jobsGroup.getId());
            temp.setAddress(jobsGroup.getAddress() + JobsConstant.COMMA
                    + registryParam.getRegistryValue());
            return updateById(temp);
        }
        return false;
    }
}
