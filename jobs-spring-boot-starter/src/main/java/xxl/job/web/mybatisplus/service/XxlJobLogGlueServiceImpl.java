package xxl.job.web.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import xxl.job.web.entity.XxlJobLogGlue;
import xxl.job.web.mybatisplus.mapper.XxlJobLogGlueMapper;
import xxl.job.web.service.IXxlJobLogGlueService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class XxlJobLogGlueServiceImpl implements IXxlJobLogGlueService<IPage> {
    @Resource
    private XxlJobLogGlueMapper jobLogGlueMapper;

    @Override
    public IPage page(HttpServletRequest request, XxlJobLogGlue jobLogGlue) {
        return jobLogGlueMapper.selectPage(XxlJobPageHelper.getPage(request),
                Wrappers.<XxlJobLogGlue>lambdaQuery().setEntity(jobLogGlue));
    }

    @Override
    public boolean removeById(int id) {
        return jobLogGlueMapper.deleteById(id) > 0;
    }
}
