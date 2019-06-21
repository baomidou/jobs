package xxl.job.web.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 管理任务组表
 *
 * @author 青苗
 * @since 2019-05-30
 */
@Data
@Accessors(chain = true)
public class XxlJobGroup implements Serializable {
    private Integer id;
    private String appName;
    private String title;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 执行器地址类型：0=自动注册、1=手动录入
     */
    private Integer addressType;
    /**
     * 执行器地址列表，多地址逗号分隔(手动录入)
     */
    private String addressList;

}
