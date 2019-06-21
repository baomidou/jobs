package xxl.job.web.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xxl.job.web.entity.XxlJobInfo;
import xxl.job.web.entity.dto.XxlJobHandleCodeDto;

import java.util.List;

/**
 * 任务信息 Mapper
 *
 * @author 青苗
 * @since 2019-05-31
 */
@Mapper
public interface XxlJobInfoMapper extends BaseMapper<XxlJobInfo> {

     @Select("SELECT handle_code,COUNT(1) AS num FROM xxl_job_log GROUP BY handle_code")
     List<XxlJobHandleCodeDto> selectHandleCodeDto();
}
