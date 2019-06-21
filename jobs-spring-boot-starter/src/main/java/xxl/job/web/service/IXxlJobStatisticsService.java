package xxl.job.web.service;

import xxl.job.web.entity.vo.XxlJobDateDistributionVO;
import xxl.job.web.entity.vo.XxlJobImportantNumVO;
import xxl.job.web.entity.vo.XxlJobSuccessRatioVO;

import java.util.List;

/**
 * 统计接口
 */
public interface IXxlJobStatisticsService {

    /**
     * 重要数量统计
     */
    XxlJobImportantNumVO getImportantNum();

    /**
     * 成功比例统计
     */
    XxlJobSuccessRatioVO getSuccessRatio();

    /**
     * 日期分布统计
     */
    List<XxlJobDateDistributionVO> getDateDistribution();

}
