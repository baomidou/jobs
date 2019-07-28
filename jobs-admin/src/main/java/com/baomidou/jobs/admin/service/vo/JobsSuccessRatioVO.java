package com.baomidou.jobs.admin.service.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 成功比例统计 VO
 *
 * @author xxl jobob
 * @since 2019-06-15
 */
@Data
@Accessors(chain = true)
public class JobsSuccessRatioVO {
    /**
     * 成功
     */
    private Integer successful;
    /**
     * 失败
     */
    private Integer  failed;

}
