package xxl.job.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xxl.job.web.R;
import xxl.job.web.entity.XxlJobRegistry;
import xxl.job.web.service.IXxlJobRegistryService;

/**
 * 用户信息
 *
 * @author 青苗
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/job-registry")
public class JobRegistryController extends BaseController {
    @Autowired
    private IXxlJobRegistryService jobRegistryService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<Object> page(XxlJobRegistry jobRegistry) {
        return success(jobRegistryService.page(request, jobRegistry));
    }
}
