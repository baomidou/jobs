package xxl.job.web.mybatisplus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xxl.job.web.entity.XxlJobGroup;
import xxl.job.web.entity.dto.XxlJobHandleCodeDto;
import xxl.job.web.entity.vo.XxlJobDateDistributionVO;
import xxl.job.web.entity.vo.XxlJobImportantNumVO;
import xxl.job.web.entity.vo.XxlJobSuccessRatioVO;
import xxl.job.web.service.IXxlJobGroupService;
import xxl.job.web.service.IXxlJobInfoService;
import xxl.job.web.service.IXxlJobLogService;
import xxl.job.web.service.IXxlJobStatisticsService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class XxlJobStatisticsServiceImpl implements IXxlJobStatisticsService {
    @Autowired
    private IXxlJobInfoService jobInfoService;
    @Autowired
    private IXxlJobLogService jobLogService;
    @Autowired
    private IXxlJobGroupService jobGroupService;

    @Override
    public XxlJobImportantNumVO getImportantNum() {
        XxlJobImportantNumVO vo = new XxlJobImportantNumVO();
        vo.setRunTaskNum(jobLogService.countAll());
        vo.setTriggeredNum(jobLogService.countSuccess());
        int onlineExecutorNum = 0;
        List<XxlJobGroup> jobGroupList = jobGroupService.listAll();
        if (jobGroupList != null && !jobGroupList.isEmpty()) {
            Set<String> addressSet = new HashSet<>();
            for (XxlJobGroup jobGroup : jobGroupList) {
                String addressList = jobGroup.getAddressList();
                if (null != addressList) {
                    addressSet.addAll(Arrays.asList(addressList.split(",")));
                }
            }
            onlineExecutorNum = addressSet.size();
        }
        vo.setOnlineExecutorNum(onlineExecutorNum);
        return vo;
    }

    @Override
    public XxlJobSuccessRatioVO getSuccessRatio() {
        List<XxlJobHandleCodeDto> handleCodeVOList = jobInfoService.getHandleCodeDto();
        if (null == handleCodeVOList || handleCodeVOList.isEmpty()) {
            return null;
        }
        XxlJobSuccessRatioVO vo = new XxlJobSuccessRatioVO();
        int size = handleCodeVOList.size();
        for (int i=0; i < size; i++) {
            XxlJobHandleCodeDto dto = handleCodeVOList.get(i);
            if(0 == dto.getHandleCode()){
                vo.setInProgress(dto.getNum());
            } else if(200 == dto.getHandleCode()){
                vo.setSuccessful(dto.getNum());
            } else if(500 == dto.getHandleCode()){
                vo.setFailed(dto.getNum());
            }
        }
        if(null == vo.getSuccessful()){
            vo.setSuccessful(0);
        }
        if(null == vo.getInProgress()){
            vo.setInProgress(0);
        }
        if(null == vo.getFailed()){
            vo.setFailed(0);
        }
        return vo;
    }

    @Override
    public List<XxlJobDateDistributionVO> getDateDistribution() {
        return null;
    }
}
