package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.baomidou.jobs.starter.entity.JobsLogGlue;
import com.baomidou.jobs.starter.mybatisplus.mapper.JobsLogGlueMapper;
import com.baomidou.jobs.starter.service.IJobsLogGlueService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class JobsLogGlueServiceImpl implements IJobsLogGlueService<IPage> {
    @Resource
    private JobsLogGlueMapper jobLogGlueMapper;

    @Override
    public IPage page(HttpServletRequest request, JobsLogGlue jobLogGlue) {
        return jobLogGlueMapper.selectPage(JobsPageHelper.getPage(request),
                Wrappers.<JobsLogGlue>lambdaQuery().setEntity(jobLogGlue));
    }

    @Override
    public boolean removeById(int id) {
        return jobLogGlueMapper.deleteById(id) > 0;
    }
}
