package xxl.job.web.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 任务日志表
 *
 * @author 青苗
 * @since 2019-05-30
 */
@Data
@Accessors(chain = true)
public class XxlJobLogGlue implements Serializable {
	private Integer id;
	/**
	 * 任务主键ID
	 */
	private Integer jobId;
	/**
	 * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
	 */
	private String glueType;
	private String glueSource;
	private String glueRemark;
	private String addTime;
	private String updateTime;

}
