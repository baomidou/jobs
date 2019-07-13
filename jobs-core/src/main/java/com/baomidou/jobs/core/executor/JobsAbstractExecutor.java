package com.baomidou.jobs.core.executor;

import com.baomidou.jobs.core.JobsConstant;
import com.baomidou.jobs.core.executor.impl.JobsExecutorImpl;
import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.thread.ExecutorRegistryThread;
import com.baomidou.jobs.core.thread.JobsThread;
import com.baomidou.jobs.core.web.IJobsAdmin;
import com.xxl.rpc.registry.ServiceRegistry;
import com.xxl.rpc.remoting.invoker.XxlRpcInvokerFactory;
import com.xxl.rpc.remoting.invoker.call.CallType;
import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.xxl.rpc.remoting.invoker.route.LoadBalance;
import com.xxl.rpc.remoting.net.NetEnum;
import com.xxl.rpc.remoting.provider.XxlRpcProviderFactory;
import com.xxl.rpc.serialize.Serializer;
import com.xxl.rpc.util.IpUtil;
import com.xxl.rpc.util.NetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jobs Executor
 *
 * @author xxl jobob
 * @since 2019-06-22
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
        if (JOBS_THREAD.size() > 0) {
            for (Map.Entry<Integer, JobsThread> item : JOBS_THREAD.entrySet()) {
                removeJobsThread(item.getKey(), "web container destroy and kill the job.");
            }
            JOBS_THREAD.clear();
        }
        JOBS_HANDLER.clear();

        // destory executor-server
        stopRpcProvider();

        // destory invoker
        stopInvokerFactory();
    }


    /**
     * Jobs Admin
     */
    private static List<IJobsAdmin> JOBS_ADMIN;
    private static Serializer serializer;

    private void initJobsAdminList(String adminAddress, String accessToken) throws Exception {
        serializer = Serializer.SerializeEnum.HESSIAN.getSerializer();
        if (!StringUtils.isEmpty(adminAddress)) {
            if (JOBS_ADMIN == null) {
                JOBS_ADMIN = new ArrayList<>();
            }
            String[] addressArr = adminAddress.trim().split(JobsConstant.COMMA);
            for (String address : addressArr) {
                if (address != null && address.trim().length() > 0) {
                    String addressUrl = address.concat(JobsConstant.JOBS_API);
                    IJobsAdmin jobsAdmin = (IJobsAdmin) new XxlRpcReferenceBean(
                            NetEnum.NETTY_HTTP,
                            serializer,
                            CallType.SYNC,
                            LoadBalance.ROUND,
                            IJobsAdmin.class,
                            null,
                            10000,
                            addressUrl,
                            accessToken,
                            null,
                            null
                    ).getObject();
                    JOBS_ADMIN.add(jobsAdmin);
                }
            }
        }
    }

    private void stopInvokerFactory() {
        // stop invoker factory
        try {
            XxlRpcInvokerFactory.getInstance().stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static List<IJobsAdmin> getJobsAdminList() {
        return JOBS_ADMIN;
    }

    public static Serializer getSerializer() {
        return serializer;
    }


    /**
     * rpc provider factory
     */
    private XxlRpcProviderFactory XXL_RPC_PROVIDER_FACTORY = null;

    private void initRpcProvider(String ip, int port, String appName, String accessToken) throws Exception {

        // init, provider factory
        Map<String, String> serviceRegistryParam = new HashMap<>(16);
        serviceRegistryParam.put("appName", appName);
        serviceRegistryParam.put("address", IpUtil.getIpPort(ip, port));

        XXL_RPC_PROVIDER_FACTORY = new XxlRpcProviderFactory();
        XXL_RPC_PROVIDER_FACTORY.initConfig(NetEnum.NETTY_HTTP, Serializer.SerializeEnum.HESSIAN.getSerializer(),
                ip, port, accessToken, ExecutorServiceRegistry.class, serviceRegistryParam);

        // add services
        XXL_RPC_PROVIDER_FACTORY.addService(IJobsExecutor.class.getName(), null, new JobsExecutorImpl());

        // start
        XXL_RPC_PROVIDER_FACTORY.start();

    }

    /**
     * RPC Client 节点注册
     */
    public static class ExecutorServiceRegistry extends ServiceRegistry {

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
        // stop provider factory
        try {
            XXL_RPC_PROVIDER_FACTORY.stop();
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


    /**
     * jobsThread cache
     */
    private static Map<Integer, JobsThread> JOBS_THREAD = new ConcurrentHashMap<>();

    public static JobsThread putJobsThread(int jobId, IJobsHandler handler, String removeOldReason) {
        JobsThread newJobThread = new JobsThread(jobId, handler);
        newJobThread.start();
        log.debug("Jobs register JobsThread success, jobId:{}, handler:{}", new Object[]{jobId, handler});

        JobsThread oldJobThread = JOBS_THREAD.put(jobId, newJobThread);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
        return newJobThread;
    }

    public static void removeJobsThread(int jobId, String removeOldReason) {
        JobsThread oldJobThread = JOBS_THREAD.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
    }

    public static JobsThread getJobsThread(int jobId) {
        return JOBS_THREAD.get(jobId);
    }
}
