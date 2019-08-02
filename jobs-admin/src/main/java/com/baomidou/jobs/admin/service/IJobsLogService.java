package com.baomidou.jobs.admin.service;

import com.baomidou.jobs.admin.service.vo.JobsDateDistributionVO;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 任务日志接口
 *
 * @author jobob
 * @since 2019-07-18
 */
public interface IJobsLogService {

    /**
     * 任务执行总数
     *
     * @return
     */
    int countAll();

    /**
     * 执行成功日志记录总数
     */
    int countSuccess();

    JobsLog getById(Long id);

    boolean updateById(JobsLog jobsInfo);

    boolean save(JobsLog jobsInfo);

    /**
     *
     * @param id
     * @return
     */
    boolean removeById(Long id);

    R<IPage<JobsLog>> page(HttpServletRequest request, JobsLog jobsInfo);

    List<JobsDateDistributionVO> getJobsDateDistributionVO();
}
