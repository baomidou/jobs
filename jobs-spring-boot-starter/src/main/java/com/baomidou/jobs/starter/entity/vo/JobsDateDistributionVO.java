package com.baomidou.jobs.starter.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 日期分布统计 VO
 *
 * @author 青苗
 * @since 2019-06-15
 */
@Data
@Accessors(chain = true)
public class JobsDateDistributionVO extends JobsSuccessRatioVO {
    /**
     * 日期
     */
    private String atDate;

}
