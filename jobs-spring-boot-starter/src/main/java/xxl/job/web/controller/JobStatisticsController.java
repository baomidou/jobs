package xxl.job.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xxl.job.web.R;
import xxl.job.web.entity.vo.XxlJobImportantNumVO;
import xxl.job.web.entity.vo.XxlJobSuccessRatioVO;
import xxl.job.web.service.IXxlJobStatisticsService;

/**
 * 统计信息
 *
 * @author 青苗
 * @since 2019-06-15
 */
@RestController
@RequestMapping("/v1/statistics")
public class JobStatisticsController extends BaseController {
    @Autowired
    private IXxlJobStatisticsService statisticsService;

    /**
     * 重要参数数量
     */
    @GetMapping("/important-num")
    public R<XxlJobImportantNumVO> importantNum() {
        return success(statisticsService.getImportantNum());
    }
    /**
     * 成功比例
     */
    @GetMapping("/success-ratio")
    public R<XxlJobSuccessRatioVO> successRatio() {
        return success(statisticsService.getSuccessRatio());
    }
}
