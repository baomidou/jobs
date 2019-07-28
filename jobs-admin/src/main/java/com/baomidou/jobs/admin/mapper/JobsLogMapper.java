package com.baomidou.jobs.admin.mapper;

import com.baomidou.jobs.admin.service.vo.JobsDateTempVO;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 日志信息 Mapper
 *
 * @author jobob
 * @since 2019-05-31
 */
@Mapper
public interface JobsLogMapper extends BaseMapper<JobsLog> {

    /**
     * 查询任务日期分布
     *
     * @return
     */
    @Select("SELECT t.num,t.code,t.ct AS at_date FROM (SELECT COUNT(1) num, trigger_code code,FROM_UNIXTIME(create_time / 1000,  '%Y-%m-%d') ct  FROM jobs_log GROUP BY trigger_code, ct) AS t,\n" +
            "(select FROM_UNIXTIME(create_time / 1000,  '%Y-%m-%d') tm from jobs_log GROUP BY tm ORDER BY tm DESC LIMIT 7) AS s\n" +
            "WHERE t.ct=s.tm ORDER BY t.ct")
    List<JobsDateTempVO> selectJobsDateTempVO();
}
