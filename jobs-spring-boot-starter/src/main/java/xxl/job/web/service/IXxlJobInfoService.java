package xxl.job.web.service;

import xxl.job.web.entity.XxlJobInfo;
import xxl.job.web.entity.dto.XxlJobHandleCodeDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IXxlJobInfoService<P> {

    /**
     * 分页
     *
     * @param request 当前请求
     * @param jobInfo 实体对象
     * @return
     */
    P page(HttpServletRequest request, XxlJobInfo jobInfo);

    List<XxlJobInfo> getJobsByGroup(int jobGroup);

    /**
     * 执行任务总数
     *
     * @return
     */
    int count();

    /**
     * 任务数
     *
     * @return
     */
    int count(int jobGroupId, int triggerStatus);

    List<XxlJobInfo> scheduleJobQuery(long maxNextTime);

    boolean updateById(XxlJobInfo jobInfo);

    /**
     * 执行、指定 ID 任务
     *
     * @param id    主键 ID
     * @param param 执行参数
     * @return
     */
    boolean execute(int id, String param);

    /**
     * 启动、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean start(int id);

    /**
     * 停止、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean stop(int id);

    /**
     * 删除、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean remove(int id);

    /**
     * HandleCode GroupBy Dto
     *
     * @return
     */
    List<XxlJobHandleCodeDto> getHandleCodeDto();

    XxlJobInfo getById(int id);
}
