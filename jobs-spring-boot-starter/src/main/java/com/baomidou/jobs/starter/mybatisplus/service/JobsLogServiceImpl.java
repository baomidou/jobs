package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.baomidou.jobs.starter.entity.JobsLog;
import com.baomidou.jobs.starter.mybatisplus.mapper.JobsLogMapper;
import com.baomidou.jobs.starter.service.IJobsLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class JobsLogServiceImpl implements IJobsLogService<IPage> {
    @Resource
    private JobsLogMapper jobLogMapper;

    @Override
    public IPage page(HttpServletRequest request, JobsLog jobLog) {
        return jobLogMapper.selectPage(JobsPageHelper.getPage(request),
                Wrappers.<JobsLog>lambdaQuery().setEntity(jobLog)
                .orderByDesc(JobsLog::getTriggerTime));
    }

    @Override
    public int countAll() {
        return jobLogMapper.selectCount(null);
    }

    @Override
    public int countSuccess() {
        return jobLogMapper.selectCount(Wrappers.<JobsLog>lambdaQuery()
                .eq(JobsLog::getHandleCode, 200));
    }

    @Override
    public int updateAlarmStatus(int logId, int oldAlarmStatus, int newAlarmStatus) {
        return jobLogMapper.update(null, Wrappers.<JobsLog>lambdaUpdate()
                .set(JobsLog::getAlarmStatus, newAlarmStatus)
                .eq(JobsLog::getAlarmStatus, oldAlarmStatus)
                .eq(JobsLog::getId, logId));
    }

    @Override
    public List<Object> listFailIds(int size) {
        return jobLogMapper.selectObjs(Wrappers.<JobsLog>lambdaQuery().select(JobsLog::getId)
                .nested(q -> q.eq(JobsLog::getHandleCode, 0).in(JobsLog::getTriggerCode, 0, 200))
                .or().eq(JobsLog::getHandleCode, 200)
                .orderByAsc(JobsLog::getId));
    }

    @Override
    public JobsLog getById(int id) {
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
    public boolean removeById(int id) {
        return jobLogMapper.deleteById(id) > 0;
    }
}