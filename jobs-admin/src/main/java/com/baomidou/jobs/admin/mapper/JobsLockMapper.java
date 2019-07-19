package com.baomidou.jobs.admin.mapper;

import com.baomidou.jobs.model.JobsLock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 锁信息 Mapper
 *
 * @author jobob
 * @since 2019-07-13
 */
@Mapper
public interface JobsLockMapper extends BaseMapper<JobsLock> {

}
