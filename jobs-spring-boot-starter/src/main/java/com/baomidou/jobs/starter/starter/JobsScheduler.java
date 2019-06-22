package com.baomidou.jobs.starter.starter;

import com.baomidou.jobs.core.biz.AdminBiz;
import com.baomidou.jobs.core.biz.ExecutorBiz;
import com.baomidou.jobs.starter.monitor.JobsFailMonitor;
import com.baomidou.jobs.starter.monitor.JobsRegistryMonitor;
import com.baomidou.jobs.starter.monitor.JobsScheduleHelper;
import com.baomidou.jobs.starter.JobsHelper;
import com.baomidou.jobs.starter.trigger.JobsTriggerPool;
import com.xxl.rpc.remoting.invoker.XxlRpcInvokerFactory;
import com.xxl.rpc.remoting.invoker.call.CallType;
import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.xxl.rpc.remoting.invoker.route.LoadBalance;
import com.xxl.rpc.remoting.net.NetEnum;
import com.xxl.rpc.remoting.net.impl.servlet.server.ServletServerHandler;
import com.xxl.rpc.remoting.provider.XxlRpcProviderFactory;
import com.xxl.rpc.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xuxueli 2018-10-28 00:18:17
 */
@Configuration
public class JobsScheduler implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(JobsScheduler.class);


    @Override
    public void afterPropertiesSet() throws Exception {
        // admin registry monitor run
        JobsRegistryMonitor.getInstance().start();

        // admin monitor run
        JobsFailMonitor.getInstance().start();

        // admin-server
        initRpcProvider();

        // start-schedule
        JobsScheduleHelper.getInstance().start();

        logger.info(">>>>>>>>> init jobs admin success.");
    }

    @Override
    public void destroy() throws Exception {
        // stop-schedule
        JobsScheduleHelper.getInstance().toStop();

        // admin trigger pool stop
        JobsTriggerPool.toStop();

        // admin registry stop
        JobsRegistryMonitor.getInstance().toStop();

        // admin monitor stop
        JobsFailMonitor.getInstance().toStop();

        // admin-server
        stopRpcProvider();
    }

    /**
     * ---------------------- admin rpc provider (no server version) ----------------------
     */
    private static ServletServerHandler servletServerHandler;

    private void initRpcProvider() {
        // init
        XxlRpcProviderFactory xxlRpcProviderFactory = new XxlRpcProviderFactory();
        xxlRpcProviderFactory.initConfig(
                NetEnum.NETTY_HTTP,
                Serializer.SerializeEnum.HESSIAN.getSerializer(),
                null,
                0,
                JobsHelper.getJobProperties().getAccessToken(),
                null,
                null);

        // add services
        xxlRpcProviderFactory.addService(AdminBiz.class.getName(), null, JobsHelper.getAdminBiz());

        // servlet handler
        servletServerHandler = new ServletServerHandler(xxlRpcProviderFactory);
    }

    private void stopRpcProvider() throws Exception {
        XxlRpcInvokerFactory.getInstance().stop();
    }

    public static void invokeAdminService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        servletServerHandler.handle(null, request, response);
    }


    /**
     * ---------------------- executor-client ----------------------
     */
    private static ConcurrentHashMap<String, ExecutorBiz> executorBizRepository = new ConcurrentHashMap<String, ExecutorBiz>();

    public static ExecutorBiz getExecutorBiz(String address) throws Exception {
        // valid
        if (address == null || address.trim().length() == 0) {
            return null;
        }

        // load-cache
        address = address.trim();
        ExecutorBiz executorBiz = executorBizRepository.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }

        // set-cache
        executorBiz = (ExecutorBiz) new XxlRpcReferenceBean(
                NetEnum.NETTY_HTTP,
                Serializer.SerializeEnum.HESSIAN.getSerializer(),
                CallType.SYNC,
                LoadBalance.ROUND,
                ExecutorBiz.class,
                null,
                5000,
                address,
                JobsHelper.getJobProperties().getAccessToken(),
                null,
                null).getObject();

        executorBizRepository.put(address, executorBiz);
        return executorBiz;
    }

}
