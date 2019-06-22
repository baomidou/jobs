package com.baomidou.jobs.starter.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.jobs.starter.entity.JobsInfo;
import com.baomidou.jobs.starter.entity.dto.JobsHandleCodeDto;

import java.util.List;

/**
 * 任务信息 Mapper
 *
 * @author 青苗
 * @since 2019-05-31
 */
@Mapper
public interface JobsInfoMapper extends BaseMapper<JobsInfo> {

     @Select("SELECT handle_code,COUNT(1) AS num FROM jobs_log GROUP BY handle_code")
     List<JobsHandleCodeDto> selectHandleCodeDto();
}
