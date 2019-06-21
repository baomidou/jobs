package xxl.job.web.starter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xxl.job.web.spring.XxlJobHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动 Redis 分布式锁
 *
 * @author hubin
 * @since 2019-07-23
 */
@Configuration
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({XxlJobHelper.class, XxlJobScheduler.class, XxlJobMybatisPlusAutoConfiguration.class})
public @interface EnableXxlJobMybatisPlus {

}
