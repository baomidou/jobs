package xxl.job.web.spring;

import com.xxl.job.core.biz.AdminBiz;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import xxl.job.web.handler.IXxlJobAlarmHandler;
import xxl.job.web.service.*;
import xxl.job.web.starter.XxlJobProperties;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Spring Boot 相关辅助类
 *
 * @author 青苗
 * @since 2019-06-08
 */
@Configuration
public class XxlJobHelper implements InitializingBean {

    private static XxlJobHelper JOB_HELPER = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        JOB_HELPER = this;
    }

    @Resource
    private IXxlJobGroupService _jobGroupService;
    @Resource
    private IXxlJobInfoService _jobInfoService;
    @Resource
    private IXxlJobLogService _jobLogService;
    @Resource
    private IXxlJobLogGlueService _jobLogGlueService;
    @Resource
    private IXxlJobRegistryService _jobRegistryService;
    @Resource
    private IXxlJobAlarmHandler _jobAlarmHandler;
    @Resource
    private XxlJobProperties _jobProperties;
    @Resource
    private AdminBiz _adminBiz;
    @Resource
    private DataSource _dataSource;

    public static XxlJobProperties getJobProperties() {
        return JOB_HELPER._jobProperties;
    }

    public static IXxlJobGroupService getJobGroupService() {
        return JOB_HELPER._jobGroupService;
    }

    public static IXxlJobInfoService getJobInfoService() {
        return JOB_HELPER._jobInfoService;
    }

    public static IXxlJobLogGlueService getJobLogGlueService() {
        return JOB_HELPER._jobLogGlueService;
    }

    public static IXxlJobLogService getJobLogService() {
        return JOB_HELPER._jobLogService;
    }

    public static IXxlJobRegistryService getJobRegistryService() {
        return JOB_HELPER._jobRegistryService;
    }

    public static IXxlJobAlarmHandler getJobAlarmHandler() {
        return JOB_HELPER._jobAlarmHandler;
    }

    public static AdminBiz getAdminBiz() {
        return JOB_HELPER._adminBiz;
    }

    public static DataSource getDataSource() {
        return JOB_HELPER._dataSource;
    }
}
