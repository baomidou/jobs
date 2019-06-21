package xxl.job.web.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Job 启动参数
 *
 * @author 青苗
 * @since 2019-06-08
 */
@Data
@ConfigurationProperties(XxlJobProperties.PREFIX)
public class XxlJobProperties {
    public static final String PREFIX = "xxl-job";
    /**
     * 访问票据
     */
    private String accessToken;

}
