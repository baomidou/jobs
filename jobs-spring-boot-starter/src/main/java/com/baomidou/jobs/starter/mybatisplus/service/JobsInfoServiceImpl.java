package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.jobs.starter.cron.CronExpression;
import com.baomidou.jobs.starter.entity.JobsInfo;
import com.baomidou.jobs.starter.entity.dto.JobsHandleCodeDto;
import com.baomidou.jobs.starter.mybatisplus.mapper.JobsInfoMapper;
import com.baomidou.jobs.starter.service.IJobsInfoService;
import com.baomidou.jobs.starter.service.IJobsLogGlueService;
import com.baomidou.jobs.starter.service.IJobsLogService;
import com.baomidou.jobs.starter.trigger.JobsTrigger;
import com.baomidou.jobs.starter.trigger.TriggerTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JobsInfoServiceImpl implements IJobsInfoService<IPage> {
    @Resource
    private JobsInfoMapper jobInfoMapper;
    @Autowired
    private IJobsLogService jobLogService;
    @Autowired
    private IJobsLogGlueService jobLogGlueService;

    @Override
    public IPage page(HttpServletRequest request, JobsInfo jobInfo) {
        return jobInfoMapper.selectPage(JobsPageHelper.getPage(request),
                Wrappers.<JobsInfo>lambdaQuery().setEntity(jobInfo));
    }

    @Override
    public List<JobsInfo> getJobsByGroup(int jobGroup) {
        return jobInfoMapper.selectList(Wrappers.<JobsInfo>lambdaQuery()
                .eq(JobsInfo::getJobGroup, jobGroup));
    }

    @Override
    public int count() {
        return jobInfoMapper.selectCount(null);
    }

    @Override
    public int count(int jobGroupId, int triggerStatus) {
        return jobInfoMapper.selectCount(Wrappers.<JobsInfo>lambdaQuery()
                .eq(JobsInfo::getJobGroup, jobGroupId)
                .eq(JobsInfo::getTriggerStatus, triggerStatus));
    }

    @Override
    public List<JobsInfo> scheduleJobQuery(long maxNextTime) {
        return jobInfoMapper.selectList(Wrappers.<JobsInfo>lambdaQuery()
                .le(JobsInfo::getTriggerNextTime, maxNextTime));
    }

    @Override
    public boolean updateById(JobsInfo jobInfo) {
        return jobInfoMapper.updateById(jobInfo) > 0;
    }

    @Override
    public boolean execute(int id, String param) {
//        JobsTriggerPool.trigger(id, TriggerTypeEnum.MANUAL, -1, null, param);
        // 直接触发执行
        JobsTrigger.trigger(id, TriggerTypeEnum.MANUAL, -1, null, param);
        return true;
    }

    @Override
    public boolean start(int id) {
        JobsInfo dbJobInfo = getById(id);
        if (null == dbJobInfo) {
            return false;
        }
        JobsInfo jobInfo = new JobsInfo();
        jobInfo.setId(dbJobInfo.getId());

        // next trigger time (10s后生效，避开预读周期)
        long nextTriggerTime;
        try {
            nextTriggerTime = new CronExpression(dbJobInfo.getJobCron())
                    .getNextValidTimeAfter(new Date(System.currentTimeMillis() + 10000)).getTime();
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return false;
        }

        jobInfo.setTriggerStatus(1);
        jobInfo.setTriggerLastTime(0L);
        jobInfo.setTriggerNextTime(nextTriggerTime);
        return jobInfoMapper.updateById(jobInfo) > 0;
    }

    @Override
    public boolean stop(int id) {
        JobsInfo jobInfo = new JobsInfo();
        jobInfo.setId(id);
        jobInfo.setTriggerStatus(0);
        jobInfo.setTriggerLastTime(0L);
        jobInfo.setTriggerNextTime(0L);
        return jobInfoMapper.updateById(jobInfo) > 0;
    }

    @Override
    public boolean remove(int id) {
        jobLogService.removeById(id);
        jobLogGlueService.removeById(id);
        return jobInfoMapper.deleteById(id) > 0;
    }

    @Override
    public List<JobsHandleCodeDto> getHandleCodeDto() {
        return jobInfoMapper.selectHandleCodeDto();
    }

    @Override
    public JobsInfo getById(int id) {
        return jobInfoMapper.selectById(id);
    }
}
