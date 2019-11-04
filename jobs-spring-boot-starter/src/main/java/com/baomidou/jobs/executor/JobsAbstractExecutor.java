package com.baomidou.jobs.executor;

import com.baomidou.jobs.JobsConstant;
import com.baomidou.jobs.handler.IJobsHandler;
import com.baomidou.jobs.rpc.registry.IJobsServiceRegistry;
import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.remoting.invoker.call.CallType;
import com.baomidou.jobs.rpc.remoting.invoker.reference.JobsRpcReferenceBean;
import com.baomidou.jobs.rpc.remoting.invoker.route.LoadBalance;
import com.baomidou.jobs.rpc.remoting.net.NetEnum;
import com.baomidou.jobs.rpc.remoting.provider.JobsRpcProviderFactory;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.baomidou.jobs.rpc.util.IpUtil;
import com.baomidou.jobs.rpc.util.NetUtil;
import com.baomidou.jobs.service.IJobsService;
import com.baomidou.jobs.thread.ExecutorRegistryThread;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jobs Executor
 *
 * @author jobob
 * @since 2019-07-16
 */
@Slf4j
@Data
public abstract class JobsAbstractExecutor {
    /**
     * jobs admin address, such as "http://address" or "http://address01,http://address02"
     */
    private String adminAddress;
    /**
     * 服务 APP
     */
    private String app;
    /**
     * IP 地址
     */
    private String ip;
    /**
     * 端口
     */
    private int port;
    /**
     * 访问 Token
     */
    private String accessToken;

    /**
     * 启动
     *
     * @throws Exception
     */
    public void start() throws Exception {

        // init invoker, admin-client
        initJobsAdminList(adminAddress, accessToken);

        // init executor-server
        port = port > 0 ? port : NetUtil.findAvailablePort(9999);
        ip = (ip != null && ip.trim().length() > 0) ? ip : IpUtil.getIp();
        initRpcProvider(ip, port, app, accessToken);
    }

    /**
     * 销毁
     */
    public void destroy() {
        JOBS_HANDLER.clear();

        stopRpcProvider();

        stopInvokerFactory();
    }

    /**
     * 序列化接口
     */
    public abstract IJobsRpcSerializer getJobsRpcSerializer();

    /**
     * Jobs Admin
     */
    private static List<IJobsService> JOBS_SERVICE;

    private void initJobsAdminList(String adminAddress, String accessToken) throws Exception {
        if (!StringUtils.isEmpty(adminAddress)) {
            if (JOBS_SERVICE == null) {
                JOBS_SERVICE = new ArrayList<>();
            }
            String[] addressArr = adminAddress.trim().split(JobsConstant.COMMA);
            for (String address : addressArr) {
                if (address != null && address.trim().length() > 0) {
                    String addressUrl = address.concat(JobsConstant.JOBS_API);
                    IJobsService jobsAdmin = (IJobsService) new JobsRpcReferenceBean(
                            NetEnum.NETTY_HTTP,
                            getJobsRpcSerializer(),
                            CallType.SYNC,
                            LoadBalance.ROUND,
                            IJobsService.class,
                            null,
                            10000,
                            addressUrl,
                            accessToken,
                            null,
                            null
                    ).getObject();
                    JOBS_SERVICE.add(jobsAdmin);
                }
            }
        }
    }

    private void stopInvokerFactory() {
        // stop invoker factory
        try {
            JobsRpcInvokerFactory.getInstance().stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static List<IJobsService> getJobsServiceList() {
        return JOBS_SERVICE;
    }

    /**
     * rpc provider factory
     */
    private JobsRpcProviderFactory JOBS_RPC_PROVIDER_FACTORY = null;

    private void initRpcProvider(String ip, int port, String appName, String accessToken) throws Exception {
        // init, provider factory
        Map<String, String> serviceRegistryParam = new HashMap<>(16);
        serviceRegistryParam.put("appName", appName);
        serviceRegistryParam.put("address", IpUtil.getIpPort(ip, port));

        JOBS_RPC_PROVIDER_FACTORY = new JobsRpcProviderFactory();
        JOBS_RPC_PROVIDER_FACTORY.initConfig(NetEnum.NETTY_HTTP, getJobsRpcSerializer(),
                ip, port, accessToken, ExecutorServiceRegistry.class, serviceRegistryParam);

        // add services
        JOBS_RPC_PROVIDER_FACTORY.addService(IJobsExecutor.class.getName(), null, new JobsExecutor());

        // start
        JOBS_RPC_PROVIDER_FACTORY.start();

    }

    /**
     * RPC Client 节点注册
     */
    public static class ExecutorServiceRegistry implements IJobsServiceRegistry {

        @Override
        public void start(Map<String, String> param) {
            // start registry
            ExecutorRegistryThread.getInstance().start(param.get("appName"), param.get("address"));
        }

        @Override
        public void stop() {
            // stop registry
            ExecutorRegistryThread.getInstance().toStop();
        }

        @Override
        public boolean registry(Set<String> keys, String value) {
            return false;
        }

        @Override
        public boolean remove(Set<String> keys, String value) {
            return false;
        }

        @Override
        public Map<String, TreeSet<String>> discovery(Set<String> keys) {
            return null;
        }

        @Override
        public TreeSet<String> discovery(String key) {
            return null;
        }

    }

    private void stopRpcProvider() {
        try {
            JOBS_RPC_PROVIDER_FACTORY.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * jobsHandler cache
     */
    private static Map<String, IJobsHandler> JOBS_HANDLER = new ConcurrentHashMap<>();

    public static IJobsHandler putJobsHandler(String name, IJobsHandler jobHandler) {
        log.debug("jobs handler register success, name:{}", name);
        return JOBS_HANDLER.put(name, jobHandler);
    }

    public static IJobsHandler getJobsHandler(String name) {
        return JOBS_HANDLER.get(name);
    }
}
