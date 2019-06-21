package xxl.job.web.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xxl.job.web.entity.XxlJobGroup;
import xxl.job.web.mybatisplus.mapper.XxlJobGroupMapper;
import xxl.job.web.service.IXxlJobGroupService;
import xxl.job.web.service.IXxlJobInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class XxlJobGroupServiceImpl implements IXxlJobGroupService<IPage> {
    @Resource
    private XxlJobGroupMapper jobGroupMapper;
    @Autowired
    private IXxlJobInfoService jobInfoService;


    @Override
    public IPage page(HttpServletRequest request, XxlJobGroup jobGroup) {
        return jobGroupMapper.selectPage(XxlJobPageHelper.getPage(request), Wrappers.<XxlJobGroup>lambdaQuery().setEntity(jobGroup));
    }

    @Override
    public List<XxlJobGroup> listAll() {
        return jobGroupMapper.selectList(Wrappers.<XxlJobGroup>lambdaQuery().orderByAsc(XxlJobGroup::getSort));
    }

    @Override
    public List<XxlJobGroup> listByAddressType(int addressType) {
        return jobGroupMapper.selectList(Wrappers.<XxlJobGroup>lambdaQuery()
                .eq(XxlJobGroup::getAddressType, addressType)
                .orderByAsc(XxlJobGroup::getSort));
    }

    @Override
    public boolean remove(int id) {
        int count = jobInfoService.count(id, -1);
        if (count > 0) {
            return false;
        }

        List<XxlJobGroup> allList = listAll();
        if (null != allList && allList.size() == 1) {
            return false;
        }
        return jobGroupMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(XxlJobGroup group) {
        return jobGroupMapper.updateById(group) > 0;
    }

    @Override
    public XxlJobGroup getById(int id) {
        return jobGroupMapper.selectById(id);
    }
}
