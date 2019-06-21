package xxl.job.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xxl.job.web.R;
import xxl.job.web.entity.XxlJobLog;
import xxl.job.web.service.IXxlJobLogService;

/**
 * 日志信息
 *
 * @author 青苗
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/job-log")
public class JobLogController extends BaseController {
    @Autowired
    private IXxlJobLogService jobLogService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<Object> page(XxlJobLog jobLog) {
        return success(jobLogService.page(request, jobLog));
    }

    /**
     * 总执行次数
     */
    @GetMapping("/count")
    public R<Integer> count() {
        return success(jobLogService.countAll());
    }

    /**
     * 总执行成功次数
     */
    @GetMapping("/count-success")
    public R<Integer> countSuccess() {
        return success(jobLogService.countSuccess());
    }
}
