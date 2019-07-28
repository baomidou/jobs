package com.baomidou.jobs.admin.service.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 日期分布统计 VO
 *
 * @author xxl jobob
 * @since 2019-06-15
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
public class JobsDateTempVO {
    /**
     * 数量
     */
    private Integer num;
    /**
     * 编码
     */
    private Integer code;
    /**
     * 日期
     */
    private String atDate;

}
