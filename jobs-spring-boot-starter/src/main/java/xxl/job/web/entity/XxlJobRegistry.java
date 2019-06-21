package xxl.job.web.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务注册表
 *
 * @author 青苗
 * @since 2019-05-30
 */
@Data
@Accessors(chain = true)
public class XxlJobRegistry implements Serializable {
    private Integer id;
    private String registryGroup;
    private String registryKey;
    private String registryValue;
    private Date updateTime;

}
