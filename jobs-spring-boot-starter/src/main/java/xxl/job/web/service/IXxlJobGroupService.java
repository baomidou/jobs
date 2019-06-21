package xxl.job.web.service;

import xxl.job.web.entity.XxlJobGroup;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * 任务组服务接口
 *
 * @author 青苗
 * @since 2019-06-15
 */
public interface IXxlJobGroupService<P> {

    /**
     * 分页
     *
     * @param request  当前请求
     * @param jobGroup 实体对象
     * @return
     */
    P page(HttpServletRequest request, XxlJobGroup jobGroup);

    /**
     * 查询所有任务组
     */
    List<XxlJobGroup> listAll();

    /**
     * 根据 addressType 查询任务组
     */
    List<XxlJobGroup> listByAddressType(int addressType);

    /**
     * 删除、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean remove(int id);

    XxlJobGroup getById(int id);

    boolean updateById(XxlJobGroup group);
}
