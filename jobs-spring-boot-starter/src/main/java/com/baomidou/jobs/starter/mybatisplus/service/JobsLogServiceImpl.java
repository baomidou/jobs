package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.jobs.starter.entity.JobsLog;
import com.baomidou.jobs.starter.mybatisplus.mapper.JobsLogMapper;
import com.baomidou.jobs.starter.service.IJobsLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class JobsLogServiceImpl implements IJobsLogService<IPage> {
    @Resource
    private JobsLogMapper jobLogMapper;

    @Override
    public IPage page(HttpServletRequest request, JobsLog jobLog) {
        return jobLogMapper.selectPage(JobsPageHelper.getPage(request),
                Wrappers.<JobsLog>lambdaQuery().setEntity(jobLog)
                        .orderByDesc(JobsLog::getCreateTime));
    }

    @Override
    public int countAll() {
        return jobLogMapper.selectCount(null);
    }

    @Override
    public int countSuccess() {
        return jobLogMapper.selectCount(Wrappers.<JobsLog>lambdaQuery()
                .eq(JobsLog::getTriggerCode, 0));
    }

    @Override
    public JobsLog getById(Long id) {
        return jobLogMapper.selectById(id);
    }

    @Override
    public boolean updateById(JobsLog jobLog) {
        return jobLogMapper.updateById(jobLog) > 0;
    }

    @Override
    public boolean save(JobsLog jobLog) {
        return jobLogMapper.insert(jobLog) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return jobLogMapper.deleteById(id) > 0;
    }
}