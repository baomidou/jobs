package xxl.job.web.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 成功比例统计 VO
 *
 * @author 青苗
 * @since 2019-06-15
 */
@Data
@Accessors(chain = true)
public class XxlJobSuccessRatioVO {
    /**
     * 成功
     */
    private Integer successful;
    /**
     * 进行中
     */
    private Integer inProgress;
    /**
     * 失败
     */
    private Integer  failed;

}
