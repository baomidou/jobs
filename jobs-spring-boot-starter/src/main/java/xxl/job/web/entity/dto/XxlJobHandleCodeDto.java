package xxl.job.web.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * JobInfo HandleCode GroupBy VO
 *
 * @author 青苗
 * @since 2019-06-15
 */
@Data
@Accessors(chain = true)
public class XxlJobHandleCodeDto {
    private Integer handleCode;
    /**
     * Group By handleCode 数量
     */
    private Integer num;

}
