package com.baomidou.jobs.starter;

import com.baomidou.jobs.executor.IJobsExecutor;
import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.remoting.invoker.call.CallType;
import com.baomidou.jobs.rpc.remoting.invoker.reference.JobsRpcReferenceBean;
import com.baomidou.jobs.rpc.remoting.invoker.route.LoadBalance;
import com.baomidou.jobs.rpc.remoting.net.NetEnum;
import com.baomidou.jobs.rpc.remoting.net.impl.servlet.server.ServletServerHandler;
import com.baomidou.jobs.rpc.remoting.provider.JobsRpcProviderFactory;
import com.baomidou.jobs.service.IJobsService;
import com.baomidou.jobs.service.JobsHeartbeat;
import com.baomidou.jobs.service.JobsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Jobs Scheduler
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
@Slf4j
@Configuration
public class JobsScheduler implements InitializingBean, DisposableBean {

    private ScheduledExecutorService executor;

    @Override
    public void afterPropertiesSet() throws Exception {
        // init rpc
        initRpcProvider();

        /**
         * jobs 执行心跳
         * 1 秒后第一次执行，之后每隔 1 秒执行一次
         */
        executor = new ScheduledThreadPoolExecutor(5);
        executor.scheduleAtFixedRate(new JobsHeartbeat(), 1, 1, TimeUnit.SECONDS);

        // 启动清理异常注册
        JobsHelper.getJobsService().cleanTimeoutApp();
        log.debug("init jobs admin success.");
    }

    @Override
    public void destroy() throws Exception {
        // stop-schedule
        if (null != executor) {
            executor.shutdown();
        }

        // stop rpc
        stopRpcProvider();
    }

    /**
     * ---------------------- admin rpc provider (no server version) ----------------------
     */
    private static ServletServerHandler servletServerHandler;

    private void initRpcProvider() {
        // init
        JobsRpcProviderFactory jobsRpcProviderFactory = new JobsRpcProviderFactory();
        jobsRpcProviderFactory.initConfig(
                NetEnum.NETTY_HTTP,
                JobsHelper.getJobsRpcSerializer(),
                null,
                0,
                JobsHelper.getJobsProperties().getAdminAccessToken(),
                null,
                null);

        // add services
        jobsRpcProviderFactory.addService(IJobsService.class.getName(), null, JobsHelper.getJobsService());

        // servlet handler
        servletServerHandler = new ServletServerHandler(jobsRpcProviderFactory);
    }

    private void stopRpcProvider() throws Exception {
        JobsRpcInvokerFactory.getInstance().stop();
    }

    public static void invokeAdminService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        servletServerHandler.handle(null, request, response);
    }


    /**
     * ---------------------- executor-client ----------------------
     */
    private static Map<String, IJobsExecutor> JOBS_EXECUTOR = new ConcurrentHashMap<>();

    public static IJobsExecutor getJobsExecutor(String address) {
        // valid
        if (address == null || address.trim().length() == 0) {
            return null;
        }

        // load-cache
        address = address.trim();
        IJobsExecutor jobsExecutor = JOBS_EXECUTOR.get(address);
        if (jobsExecutor != null) {
            return jobsExecutor;
        }

        // set-cache
        jobsExecutor = (IJobsExecutor) new JobsRpcReferenceBean(
                NetEnum.NETTY_HTTP,
                JobsHelper.getJobsRpcSerializer(),
                CallType.SYNC,
                LoadBalance.ROUND,
                IJobsExecutor.class,
                null,
                5000,
                address,
                JobsHelper.getJobsProperties().getAppAccessToken(),
                null,
                null).getObject();

        JOBS_EXECUTOR.put(address, jobsExecutor);
        return jobsExecutor;
    }
}
