package com.baomidou.jobs.admin.mapper;

import com.baomidou.jobs.starter.entity.JobsRegistry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 注册任务信息 Mapper
 *
 * @author jobob
 * @since 2019-05-31
 */
@Mapper
public interface JobsRegistryMapper extends BaseMapper<JobsRegistry> {

    /**
     * 删除超时数据
     */
    @Delete("DELETE FROM jobs_registry WHERE update_time < DATE_ADD(NOW(),INTERVAL -#{timeout} SECOND)")
    int deleteTimeOut(@Param("timeout") int timeout);

    /**
     * 查询超时列表
     */
    @Select("SELECT * FROM jobs_registry WHERE update_time > DATE_ADD(NOW(),INTERVAL -#{timeout} SECOND)")
    List<JobsRegistry> selectTimeout(@Param("timeout") int timeout);

}
