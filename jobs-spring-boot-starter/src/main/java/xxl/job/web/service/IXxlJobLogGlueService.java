package xxl.job.web.service;

import xxl.job.web.entity.XxlJobLogGlue;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface IXxlJobLogGlueService<P extends Serializable> {

    /**
     * 分页
     *
     * @param request    当前请求
     * @param jobLogGlue 实体对象
     * @return
     */
    P page(HttpServletRequest request, XxlJobLogGlue jobLogGlue);

    boolean removeById(int id);
}
