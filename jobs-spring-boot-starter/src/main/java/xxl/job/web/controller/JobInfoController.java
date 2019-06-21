package xxl.job.web.controller;

import org.springframework.web.bind.annotation.*;
import xxl.job.web.R;
import xxl.job.web.entity.XxlJobInfo;
import xxl.job.web.service.IXxlJobInfoService;

import javax.annotation.Resource;

/**
 * 任务信息
 *
 * @author 青苗
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/job-info")
public class JobInfoController extends BaseController {
    @Resource
    private IXxlJobInfoService jobInfoService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<Object> page(XxlJobInfo jobInfo) {
        return success(jobInfoService.page(request, jobInfo));
    }

    /**
     * 总任务数
     */
    @GetMapping("/count")
    public R<Integer> count() {
        return success(jobInfoService.count());
    }

    /**
     * 执行
     */
    @PostMapping("/execute-{id}")
    public R<Boolean> execute(@PathVariable("id") int id, String param) {
        return success(jobInfoService.execute(id, param));
    }

    /**
     * 启动
     */
    @PostMapping("/start-{id}")
    public R<Boolean> start(@PathVariable("id") int id) {
        return success(jobInfoService.start(id));
    }

    /**
     * 停止
     */
    @PostMapping("/stop-{id}")
    public R<Boolean> stop(@PathVariable("id") int id) {
        return success(jobInfoService.stop(id));
    }

    /**
     * 删除
     */
    @PostMapping("/remove-{id}")
    public R<Boolean> remove(@PathVariable("id") int id) {
        return success(jobInfoService.remove(id));
    }
}
