package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.jobs.starter.entity.JobsGroup;
import com.baomidou.jobs.starter.mybatisplus.mapper.JobsGroupMapper;
import com.baomidou.jobs.starter.service.IJobsGroupService;
import com.baomidou.jobs.starter.service.IJobsInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class JobsGroupServiceImpl implements IJobsGroupService<IPage> {
    @Resource
    private JobsGroupMapper jobGroupMapper;
    @Autowired
    private IJobsInfoService jobInfoService;


    @Override
    public IPage page(HttpServletRequest request, JobsGroup jobGroup) {
        return jobGroupMapper.selectPage(JobsPageHelper.getPage(request), Wrappers.<JobsGroup>lambdaQuery().setEntity(jobGroup));
    }

    @Override
    public List<JobsGroup> listAll() {
        return jobGroupMapper.selectList(Wrappers.<JobsGroup>lambdaQuery().orderByAsc(JobsGroup::getSort));
    }

    @Override
    public List<JobsGroup> listByAddressType(int addressType) {
        return jobGroupMapper.selectList(Wrappers.<JobsGroup>lambdaQuery()
                .eq(JobsGroup::getType, addressType)
                .orderByAsc(JobsGroup::getSort));
    }

    @Override
    public boolean remove(int id) {
        int count = jobInfoService.count(id, -1);
        if (count > 0) {
            return false;
        }

        List<JobsGroup> allList = listAll();
        if (null != allList && allList.size() == 1) {
            return false;
        }
        return jobGroupMapper.deleteById(id) > 0;
    }

    @Override
    public boolean save(JobsGroup jobsGroup) {
        return jobGroupMapper.insert(jobsGroup) > 0;
    }

    @Override
    public boolean updateById(JobsGroup group) {
        return jobGroupMapper.updateById(group) > 0;
    }

    @Override
    public JobsGroup getById(int id) {
        return jobGroupMapper.selectById(id);
    }

    @Override
    public JobsGroup getByApp(String app) {
        return jobGroupMapper.selectOne(Wrappers.<JobsGroup>lambdaQuery()
                .eq(JobsGroup::getApp, app));
    }
}
