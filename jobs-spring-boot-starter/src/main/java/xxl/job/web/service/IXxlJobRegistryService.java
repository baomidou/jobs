package xxl.job.web.service;

import xxl.job.web.entity.XxlJobRegistry;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

public interface IXxlJobRegistryService<P extends Serializable> {

    /**
     * 分页
     *
     * @param request     当前请求
     * @param jobRegistry 实体对象
     * @return
     */
    P page(HttpServletRequest request, XxlJobRegistry jobRegistry);

    /**
     * 删除超时数据
     *
     * @param timeout 超时时长
     * @return
     */
    int removeTimeOut(int timeout);

    int update(String registryGroup, String registryKey, String registryValue);

    int save(String registryGroup, String registryKey, String registryValue);

    int remove(String registryGroup, String registryKey, String registryValue);

    /**
     * 超时数据列表
     *
     * @param timeout 超时时长
     * @return
     */
    List<XxlJobRegistry> listTimeout(int timeout);
}
