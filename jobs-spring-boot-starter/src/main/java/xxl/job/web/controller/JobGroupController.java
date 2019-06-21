package xxl.job.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xxl.job.web.R;
import xxl.job.web.entity.XxlJobGroup;
import xxl.job.web.service.IXxlJobGroupService;

/**
 * 任务组信息
 *
 * @author 青苗
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/job-group")
public class JobGroupController extends BaseController {
    @Autowired
    private IXxlJobGroupService jobGroupService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<Object> page(XxlJobGroup jobGroup) {
        return success(jobGroupService.page(request, jobGroup));
    }

    /**
     * 删除
     */
    @PostMapping("/remove-{id}")
    public R<Boolean> remove(@PathVariable("id") int id) {
        return success(jobGroupService.remove(id));
    }
}
