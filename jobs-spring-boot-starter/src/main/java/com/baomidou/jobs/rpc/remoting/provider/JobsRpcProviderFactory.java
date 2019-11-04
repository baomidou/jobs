package com.baomidou.jobs.rpc.remoting.provider;

import com.baomidou.jobs.exception.JobsRpcException;
import com.baomidou.jobs.rpc.registry.IJobsServiceRegistry;
import com.baomidou.jobs.rpc.remoting.net.NetEnum;
import com.baomidou.jobs.rpc.remoting.net.Server;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.baomidou.jobs.rpc.util.IpUtil;
import com.baomidou.jobs.rpc.util.NetUtil;
import com.baomidou.jobs.service.JobsHelper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * provider
 *
 * @author xuxueli 2015-10-31 22:54:27
 */
@Slf4j
public class JobsRpcProviderFactory {
    private NetEnum netType;
    private IJobsRpcSerializer serializer;
    private String ip;
    private int port;
    private String accessToken;

    private Class<? extends IJobsServiceRegistry> serviceRegistryClass;
    private Map<String, String> serviceRegistryParam;


    public JobsRpcProviderFactory() {
        // to do nothing
    }

    public void initConfig(NetEnum netType,
                           IJobsRpcSerializer serializer,
                           String ip,
                           int port,
                           String accessToken,
                           Class<? extends IJobsServiceRegistry> serviceRegistryClass,
                           Map<String, String> serviceRegistryParam) {

        // init
        this.netType = netType;
        this.serializer = serializer;
        this.ip = ip;
        this.port = port;
        this.accessToken = accessToken;
        this.serviceRegistryClass = serviceRegistryClass;
        this.serviceRegistryParam = serviceRegistryParam;

        // valid
        if (this.netType == null) {
            throw new JobsRpcException("Jobs rpc provider netType missing.");
        }
        if (this.serializer == null) {
            throw new JobsRpcException("Jobs rpc provider serializer missing.");
        }
        if (this.ip == null) {
            this.ip = IpUtil.getIp();
        }
        if (this.port <= 0) {
            this.port = 7080;
        }
        if (NetUtil.isPortUsed(this.port)) {
            throw new JobsRpcException("Jobs rpc provider port[" + this.port + "] is used.");
        }
        if (this.serviceRegistryClass != null) {
            if (this.serviceRegistryParam == null) {
                throw new JobsRpcException("Jobs rpc provider serviceRegistryParam is missing.");
            }
        }

    }


    public IJobsRpcSerializer getSerializer() {
        return serializer;
    }

    public int getPort() {
        return port;
    }


    // ---------------------- start / stop ----------------------

    private Server server;
    private IJobsServiceRegistry serviceRegistry;
    private String serviceAddress;

    public void start() throws Exception {
        // start server
        serviceAddress = IpUtil.getIpPort(this.ip, port);
        server = netType.serverClass.newInstance();
        // serviceRegistry started
        server.setStartedCallback(() -> {
            // start registry
            if (serviceRegistryClass != null) {
                try {
                    serviceRegistry = serviceRegistryClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                serviceRegistry.start(serviceRegistryParam);
                if (serviceData.size() > 0) {
                    serviceRegistry.registry(serviceData.keySet(), serviceAddress);
                }
            }
        });
        server.setStoppedCallback(() -> {
            // stop registry
            if (serviceRegistry != null) {
                if (serviceData.size() > 0) {
                    serviceRegistry.remove(serviceData.keySet(), serviceAddress);
                }
                serviceRegistry.stop();
                serviceRegistry = null;
            }
        });
        server.start(this);
    }

    public void stop() throws Exception {
        // stop server
        server.stop();
    }


    // ---------------------- server invoke ----------------------

    /**
     * init local rpc service map
     */
    private Map<String, Object> serviceData = new HashMap<>();

    public Map<String, Object> getServiceData() {
        return serviceData;
    }

    /**
     * make service key
     *
     * @param iface
     * @param version
     * @return
     */
    public static String makeServiceKey(String iface, String version) {
        String serviceKey = iface;
        if (version != null && version.trim().length() > 0) {
            serviceKey += "#".concat(version);
        }
        return serviceKey;
    }

    /**
     * add service
     *
     * @param iface
     * @param version
     * @param serviceBean
     */
    public void addService(String iface, String version, Object serviceBean) {
        String serviceKey = makeServiceKey(iface, version);
        serviceData.put(serviceKey, serviceBean);
        log.info("Jobs rpc, provider factory add service success. serviceKey = {}, serviceBean = {}", serviceKey, serviceBean.getClass());
    }

    /**
     * invoke service
     *
     * @param jobsRpcRequest
     * @return
     */
    public JobsRpcResponse invokeService(JobsRpcRequest jobsRpcRequest) {
        //  make response
        JobsRpcResponse jobsRpcResponse = new JobsRpcResponse();
        jobsRpcResponse.setRequestId(jobsRpcRequest.getRequestId());

        // match service bean
        String serviceKey = makeServiceKey(jobsRpcRequest.getClassName(), jobsRpcRequest.getVersion());
        Object serviceBean = serviceData.get(serviceKey);

        // valid
        if (null == serviceBean) {
            jobsRpcResponse.setErrorMsg("The serviceKey[" + serviceKey + "] not found.");
            return jobsRpcResponse;
        }

        if (System.currentTimeMillis() - jobsRpcRequest.getCreateMillisTime() > 3 * 60 * 1000) {
            jobsRpcResponse.setErrorMsg("The timestamp difference between admin and executor exceeds the limit.");
            return jobsRpcResponse;
        }
        if (null != accessToken && accessToken.trim().length() > 0 && !accessToken.trim().equals(jobsRpcRequest.getAccessToken())) {
            jobsRpcResponse.setErrorMsg("The access token[" + jobsRpcRequest.getAccessToken() + "] is wrong.");
            return jobsRpcResponse;
        }

        try {
            // invoke
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = jobsRpcRequest.getMethodName();
            Class<?>[] parameterTypes = jobsRpcRequest.getParameterTypes();
            Object[] parameters = jobsRpcRequest.getParameters();

            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(serviceBean, parameters);
            jobsRpcResponse.setResult(result);
        } catch (Throwable t) {
            log.error("Jobs rpc provider invokeService error.", t);
            jobsRpcResponse.setErrorMsg(JobsHelper.getErrorInfo(t));
        }
        return jobsRpcResponse;
    }
}
