package xxl.job.web.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xxl.job.web.cron.CronExpression;
import xxl.job.web.entity.XxlJobInfo;
import xxl.job.web.entity.dto.XxlJobHandleCodeDto;
import xxl.job.web.mybatisplus.mapper.XxlJobInfoMapper;
import xxl.job.web.service.IXxlJobInfoService;
import xxl.job.web.service.IXxlJobLogGlueService;
import xxl.job.web.service.IXxlJobLogService;
import xxl.job.web.trigger.TriggerTypeEnum;
import xxl.job.web.trigger.XxlJobTriggerPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class XxlJobInfoServiceImpl implements IXxlJobInfoService<IPage> {
    @Resource
    private XxlJobInfoMapper jobInfoMapper;
    @Autowired
    private IXxlJobLogService jobLogService;
    @Autowired
    private IXxlJobLogGlueService jobLogGlueService;

    @Override
    public IPage page(HttpServletRequest request, XxlJobInfo jobInfo) {
        return jobInfoMapper.selectPage(XxlJobPageHelper.getPage(request),
                Wrappers.<XxlJobInfo>lambdaQuery().setEntity(jobInfo));
    }

    @Override
    public List<XxlJobInfo> getJobsByGroup(int jobGroup) {
        return jobInfoMapper.selectList(Wrappers.<XxlJobInfo>lambdaQuery()
                .eq(XxlJobInfo::getJobGroup, jobGroup));
    }

    @Override
    public int count() {
        return jobInfoMapper.selectCount(null);
    }

    @Override
    public int count(int jobGroupId, int triggerStatus) {
        return jobInfoMapper.selectCount(Wrappers.<XxlJobInfo>lambdaQuery()
                .eq(XxlJobInfo::getJobGroup, jobGroupId)
                .eq(XxlJobInfo::getTriggerStatus, triggerStatus));
    }

    @Override
    public List<XxlJobInfo> scheduleJobQuery(long maxNextTime) {
        return jobInfoMapper.selectList(Wrappers.<XxlJobInfo>lambdaQuery()
                .le(XxlJobInfo::getTriggerNextTime, maxNextTime));
    }

    @Override
    public boolean updateById(XxlJobInfo jobInfo) {
        return jobInfoMapper.updateById(jobInfo) > 0;
    }

    @Override
    public boolean execute(int id, String param) {
        XxlJobTriggerPool.trigger(id, TriggerTypeEnum.MANUAL, -1, null, param);
        return true;
    }

    @Override
    public boolean start(int id) {
        XxlJobInfo dbJobInfo = getById(id);
        if (null == dbJobInfo) {
            return false;
        }
        XxlJobInfo jobInfo = new XxlJobInfo();
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
        XxlJobInfo jobInfo = new XxlJobInfo();
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
    public List<XxlJobHandleCodeDto> getHandleCodeDto() {
        return jobInfoMapper.selectHandleCodeDto();
    }

    @Override
    public XxlJobInfo getById(int id) {
        return jobInfoMapper.selectById(id);
    }
}
