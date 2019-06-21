package xxl.job.web.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xxl.job.web.entity.XxlJobRegistry;

import java.util.List;

/**
 * 注册任务信息 Mapper
 *
 * @author 青苗
 * @since 2019-05-31
 */
@Mapper
public interface XxlJobRegistryMapper extends BaseMapper<XxlJobRegistry> {

    /**
     * 删除超时数据
     */
    @Delete("DELETE FROM xxl_job_registry WHERE update_time < DATE_ADD(NOW(),INTERVAL -#{timeout} SECOND)")
    int deleteTimeOut(@Param("timeout") int timeout);

    /**
     * 查询超时列表
     */
    @Select("SELECT * FROM xxl_job_registry WHERE update_time > DATE_ADD(NOW(),INTERVAL -#{timeout} SECOND)")
    List<XxlJobRegistry> selectTimeout(@Param("timeout") int timeout);

}
