package xxl.job.web.service;

import xxl.job.web.entity.XxlJobLog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IXxlJobLogService<P> {

    /**
     * 分页
     *
     * @param request 当前请求
     * @param jobLog  实体对象
     * @return
     */
    P page(HttpServletRequest request, XxlJobLog jobLog);

    /**
     * 任务执行总数
     *
     * @return
     */
    int countAll();

    /**
     * 执行成功日志记录总数
     */
    int countSuccess();

    int updateAlarmStatus(int logId, int oldAlarmStatus, int newAlarmStatus);

    List<Object> listFailIds(int size);

    XxlJobLog getById(int id);

    boolean updateById(XxlJobLog jobLog);

    boolean save(XxlJobLog jobLog);

    boolean removeById(int id);
}
