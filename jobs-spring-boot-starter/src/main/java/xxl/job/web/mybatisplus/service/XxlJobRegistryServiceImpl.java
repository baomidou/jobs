package xxl.job.web.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import xxl.job.web.entity.XxlJobRegistry;
import xxl.job.web.mybatisplus.mapper.XxlJobRegistryMapper;
import xxl.job.web.service.IXxlJobRegistryService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class XxlJobRegistryServiceImpl implements IXxlJobRegistryService<IPage> {
    @Resource
    private XxlJobRegistryMapper jobRegistryMapper;

    @Override
    public IPage page(HttpServletRequest request, XxlJobRegistry jobRegistry) {
        return jobRegistryMapper.selectPage(XxlJobPageHelper.getPage(request),
                Wrappers.<XxlJobRegistry>lambdaQuery().setEntity(jobRegistry));
    }

    @Override
    public int removeTimeOut(int timeout) {
        return jobRegistryMapper.deleteTimeOut(timeout);
    }

    @Override
    public int update(String registryGroup, String registryKey, String registryValue) {
        return jobRegistryMapper.update(new XxlJobRegistry().setUpdateTime(new Date()),
                Wrappers.<XxlJobRegistry>lambdaQuery()
                        .eq(XxlJobRegistry::getRegistryGroup, registryGroup)
                        .eq(XxlJobRegistry::getRegistryKey, registryKey)
                        .eq(XxlJobRegistry::getRegistryValue, registryValue));
    }

    @Override
    public int save(String registryGroup, String registryKey, String registryValue) {
        return jobRegistryMapper.insert(new XxlJobRegistry().setRegistryGroup(registryGroup)
                .setRegistryKey(registryKey).setRegistryValue(registryValue)
                .setUpdateTime(new Date()));
    }

    @Override
    public int remove(String registryGroup, String registryKey, String registryValue) {
        return jobRegistryMapper.delete(Wrappers.<XxlJobRegistry>lambdaQuery()
                .eq(XxlJobRegistry::getRegistryGroup, registryGroup)
                .eq(XxlJobRegistry::getRegistryKey, registryKey)
                .eq(XxlJobRegistry::getRegistryValue, registryValue));
    }

    @Override
    public List<XxlJobRegistry> listTimeout(int timeout) {
        return jobRegistryMapper.selectTimeout(timeout);
    }
}
