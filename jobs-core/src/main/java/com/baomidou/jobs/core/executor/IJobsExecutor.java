package com.baomidou.jobs.core.executor;

import com.baomidou.jobs.core.runner.IJobsRunner;
import com.baomidou.jobs.core.web.IJobsAdmin;
import com.baomidou.jobs.core.runner.impl.JobsRunnerImpl;
import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.log.JobsFileAppender;
import com.baomidou.jobs.core.thread.ExecutorRegistryThread;
import com.baomidou.jobs.core.thread.JobsLogFileCleanThread;
import com.baomidou.jobs.core.thread.JobsThread;
import com.baomidou.jobs.core.thread.TriggerCallbackThread;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuxueli on 2016/3/2 21:14.
 */
@Slf4j
@Data
public class IJobsExecutor {
    private String adminAddresses;
    private String appName;
    private String ip;
    private int port;
    private String accessToken;
    private String logPath;
    private int logRetentionDays;

    // ---------------------- start + stop ----------------------
    public void start() throws Exception {

        // init logpath
        JobsFileAppender.initLogPath(logPath);

        // init invoker, admin-client
        initAdminBizList(adminAddresses, accessToken);


        // init JobsLogFileCleanThread
        JobsLogFileCleanThread.getInstance().start(logRetentionDays);

        // init TriggerCallbackThread
        TriggerCallbackThread.getInstance().start();

        // init executor-server
        port = port > 0 ? port : NetUtil.findAvailablePort(9999);
        ip = (ip != null && ip.trim().length() > 0) ? ip : IpUtil.getIp();
        initRpcProvider(ip, port, appName, accessToken);
    }

    public void destroy() {
        // destory jobThreadRepository
        if (jobThreadRepository.size() > 0) {
            for (Map.Entry<Integer, JobsThread> item : jobThreadRepository.entrySet()) {
                removeJobThread(item.getKey(), "web container destroy and kill the job.");
            }
            jobThreadRepository.clear();
        }
        jobHandlerRepository.clear();


        // destory JobsLogFileCleanThread
        JobsLogFileCleanThread.getInstance().toStop();

        // destory TriggerCallbackThread
        TriggerCallbackThread.getInstance().toStop();

        // destory executor-server
        stopRpcProvider();

        // destory invoker
        stopInvokerFactory();
    }


    // ---------------------- admin-client (rpc invoker) ----------------------
    private static List<IJobsAdmin> adminBizList;
    private static Serializer serializer;

    private void initAdminBizList(String adminAddresses, String accessToken) throws Exception {
        serializer = Serializer.SerializeEnum.HESSIAN.getSerializer();
        if (adminAddresses != null && adminAddresses.trim().length() > 0) {
            for (String address : adminAddresses.trim().split(",")) {
                if (address != null && address.trim().length() > 0) {

                    String addressUrl = address.concat(IJobsAdmin.MAPPING);

                    IJobsAdmin adminBiz = (IJobsAdmin) new XxlRpcReferenceBean(
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

                    if (adminBizList == null) {
                        adminBizList = new ArrayList<IJobsAdmin>();
                    }
                    adminBizList.add(adminBiz);
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

    public static List<IJobsAdmin> getAdminBizList() {
        return adminBizList;
    }

    public static Serializer getSerializer() {
        return serializer;
    }


    // ---------------------- executor-server (rpc provider) ----------------------
    private XxlRpcProviderFactory xxlRpcProviderFactory = null;

    private void initRpcProvider(String ip, int port, String appName, String accessToken) throws Exception {

        // init, provider factory
        String address = IpUtil.getIpPort(ip, port);
        Map<String, String> serviceRegistryParam = new HashMap<String, String>();
        serviceRegistryParam.put("appName", appName);
        serviceRegistryParam.put("address", address);

        xxlRpcProviderFactory = new XxlRpcProviderFactory();
        xxlRpcProviderFactory.initConfig(NetEnum.NETTY_HTTP, Serializer.SerializeEnum.HESSIAN.getSerializer(), ip, port, accessToken, ExecutorServiceRegistry.class, serviceRegistryParam);

        // add services
        xxlRpcProviderFactory.addService(IJobsRunner.class.getName(), null, new JobsRunnerImpl());

        // start
        xxlRpcProviderFactory.start();

    }

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
            xxlRpcProviderFactory.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    // ---------------------- job handler repository ----------------------
    private static ConcurrentHashMap<String, IJobsHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobsHandler>();

    public static IJobsHandler registJobHandler(String name, IJobsHandler jobHandler) {
        log.info(">>>>>>>>>>> jobs register jobhandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    public static IJobsHandler loadJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }


    // ---------------------- job thread repository ----------------------
    private static ConcurrentHashMap<Integer, JobsThread> jobThreadRepository = new ConcurrentHashMap<Integer, JobsThread>();

    public static JobsThread registJobThread(int jobId, IJobsHandler handler, String removeOldReason) {
        JobsThread newJobThread = new JobsThread(jobId, handler);
        newJobThread.start();
        log.info(">>>>>>>>>>> jobs regist JobsThread success, jobId:{}, handler:{}", new Object[]{jobId, handler});

        JobsThread oldJobThread = jobThreadRepository.put(jobId, newJobThread);    // putIfAbsent | oh my god, map's put method return the old value!!!
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }

        return newJobThread;
    }

    public static void removeJobThread(int jobId, String removeOldReason) {
        JobsThread oldJobThread = jobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
    }

    public static JobsThread loadJobThread(int jobId) {
        JobsThread jobThread = jobThreadRepository.get(jobId);
        return jobThread;
    }

}
