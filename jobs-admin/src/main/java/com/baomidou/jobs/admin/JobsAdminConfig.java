package com.baomidou.jobs.admin;

import com.baomidou.jobs.handler.IJobsResultHandler;
import com.baomidou.jobs.starter.EnableJobsAdmin;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jobs Admin 启动配置
 *
 * @author jobob
 * @since 2019-06-08
 */
@EnableJobsAdmin
@Configuration
public class JobsAdminConfig {

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 任务调度结果处理器，可用于失败报警成功通知
     *
     * @return
     */
    @Bean
    public IJobsResultHandler jobsResultHandler() {
        return (jobInfo, address, jobsResponse) ->
                System.out.println("Jobs 报警处理器，调度地址："
                        + address);
    }
}
