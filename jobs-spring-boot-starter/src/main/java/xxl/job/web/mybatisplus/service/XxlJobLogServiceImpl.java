package xxl.job.web.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import xxl.job.web.entity.XxlJobLog;
import xxl.job.web.mybatisplus.mapper.XxlJobLogMapper;
import xxl.job.web.service.IXxlJobLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class XxlJobLogServiceImpl implements IXxlJobLogService<IPage> {
    @Resource
    private XxlJobLogMapper jobLogMapper;

    @Override
    public IPage page(HttpServletRequest request, XxlJobLog jobLog) {
        return jobLogMapper.selectPage(XxlJobPageHelper.getPage(request),
                Wrappers.<XxlJobLog>lambdaQuery().setEntity(jobLog)
                .orderByDesc(XxlJobLog::getTriggerTime));
    }

    @Override
    public int countAll() {
        return jobLogMapper.selectCount(null);
    }

    @Override
    public int countSuccess() {
        return jobLogMapper.selectCount(Wrappers.<XxlJobLog>lambdaQuery()
                .eq(XxlJobLog::getHandleCode, 200));
    }

    @Override
    public int updateAlarmStatus(int logId, int oldAlarmStatus, int newAlarmStatus) {
        return jobLogMapper.update(null, Wrappers.<XxlJobLog>lambdaUpdate()
                .set(XxlJobLog::getAlarmStatus, newAlarmStatus)
                .eq(XxlJobLog::getAlarmStatus, oldAlarmStatus)
                .eq(XxlJobLog::getId, logId));
    }

    @Override
    public List<Object> listFailIds(int size) {
        return jobLogMapper.selectObjs(Wrappers.<XxlJobLog>lambdaQuery().select(XxlJobLog::getId)
                .nested(q -> q.eq(XxlJobLog::getHandleCode, 0).in(XxlJobLog::getTriggerCode, 0, 200))
                .or().eq(XxlJobLog::getHandleCode, 200)
                .orderByAsc(XxlJobLog::getId));
    }

    @Override
    public XxlJobLog getById(int id) {
        return jobLogMapper.selectById(id);
    }

    @Override
    public boolean updateById(XxlJobLog jobLog) {
        return jobLogMapper.updateById(jobLog) > 0;
    }

    @Override
    public boolean save(XxlJobLog jobLog) {
        return jobLogMapper.insert(jobLog) > 0;
    }

    @Override
    public boolean removeById(int id) {
        return jobLogMapper.deleteById(id) > 0;
    }
}