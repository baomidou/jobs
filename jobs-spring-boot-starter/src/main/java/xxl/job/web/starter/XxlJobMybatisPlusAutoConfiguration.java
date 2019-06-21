package xxl.job.web.starter;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xxl.job.web.handler.IXxlJobAlarmHandler;
import xxl.job.web.handler.XxlJobAlarmSimpleHandler;

/**
 * Job 启动参数
 *
 * @author 青苗
 * @since 2019-06-08
 */
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
@MapperScan("xxl.job.web.mybatisplus.mapper*")
@ComponentScan(basePackages = {
        "xxl.job.web.mybatisplus.mapper",
        "xxl.job.web.mybatisplus.service",
        "xxl.job.web.controller"
})
public class XxlJobMybatisPlusAutoConfiguration {

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public IXxlJobAlarmHandler xxlJobAlarmHandler() {
        return new XxlJobAlarmSimpleHandler();
    }
//
//    @Bean
//    public XxlJobHelper XxlJobHelper() {
//        return new XxlJobHelper();
//    }
}
