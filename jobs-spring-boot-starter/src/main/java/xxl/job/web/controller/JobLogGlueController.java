package xxl.job.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xxl.job.web.R;
import xxl.job.web.entity.XxlJobLogGlue;
import xxl.job.web.service.IXxlJobLogGlueService;

/**
 * 日志信息
 *
 * @author 青苗
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/job-log-glue")
public class JobLogGlueController extends BaseController {
    @Autowired
    private IXxlJobLogGlueService jobLogGlueService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<Object> page(XxlJobLogGlue jobLogGlue) {
        return success(jobLogGlueService.page(request, jobLogGlue));
    }
}
