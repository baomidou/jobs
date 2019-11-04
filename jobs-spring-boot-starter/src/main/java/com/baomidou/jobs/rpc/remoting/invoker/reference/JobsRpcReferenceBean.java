package com.baomidou.jobs.rpc.remoting.invoker.reference;

import com.baomidou.jobs.exception.JobsRpcException;
import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.remoting.invoker.call.CallType;
import com.baomidou.jobs.rpc.remoting.invoker.call.JobsRpcInvokeCallback;
import com.baomidou.jobs.rpc.remoting.invoker.call.JobsRpcInvokeFuture;
import com.baomidou.jobs.rpc.remoting.invoker.generic.JobsRpcGenericService;
import com.baomidou.jobs.rpc.remoting.invoker.route.LoadBalance;
import com.baomidou.jobs.rpc.remoting.net.Client;
import com.baomidou.jobs.rpc.remoting.net.NetEnum;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcFutureResponse;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import com.baomidou.jobs.rpc.remoting.provider.JobsRpcProviderFactory;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.baomidou.jobs.rpc.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * rpc reference bean, use by api
 *
 * @author xuxueli 2015-10-29 20:18:32
 */
@Slf4j
public class JobsRpcReferenceBean {
    private NetEnum netType;
    private IJobsRpcSerializer serializer;
    private CallType callType;
    private LoadBalance loadBalance;

    private Class<?> iface;
    private String version;

    private long timeout = 1000;

    private String address;
    private String accessToken;

    private JobsRpcInvokeCallback invokeCallback;

    private JobsRpcInvokerFactory invokerFactory;

    public JobsRpcReferenceBean(NetEnum netType,
                                IJobsRpcSerializer serializer,
                                CallType callType,
                                LoadBalance loadBalance,
                                Class<?> iface,
                                String version,
                                long timeout,
                                String address,
                                String accessToken,
                                JobsRpcInvokeCallback invokeCallback,
                                JobsRpcInvokerFactory invokerFactory
    ) {

        this.netType = netType;
        this.serializer = serializer;
        this.callType = callType;
        this.loadBalance = loadBalance;
        this.iface = iface;
        this.version = version;
        this.timeout = timeout;
        this.address = address;
        this.accessToken = accessToken;
        this.invokeCallback = invokeCallback;
        this.invokerFactory = invokerFactory;

        // valid
        if (this.netType == null) {
            throw new JobsRpcException("Jobs rpc reference netType missing.");
        }
        if (this.serializer == null) {
            throw new JobsRpcException("Jobs rpc reference serializer missing.");
        }
        if (this.callType == null) {
            throw new JobsRpcException("Jobs rpc reference callType missing.");
        }
        if (this.loadBalance == null) {
            throw new JobsRpcException("Jobs rpc reference loadBalance missing.");
        }
        if (this.iface == null) {
            throw new JobsRpcException("Jobs rpc reference iface missing.");
        }
        if (this.timeout < 0) {
            this.timeout = 0;
        }
        if (this.invokerFactory == null) {
            this.invokerFactory = JobsRpcInvokerFactory.getInstance();
        }

        // init Client
        initClient();
    }

    // get
    public IJobsRpcSerializer getSerializer() {
        return serializer;
    }

    public long getTimeout() {
        return timeout;
    }

    public JobsRpcInvokerFactory getInvokerFactory() {
        return invokerFactory;
    }

    // ---------------------- initClient ----------------------

    Client client = null;

    private void initClient() {
        try {
            client = netType.clientClass.newInstance();
            client.init(this);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JobsRpcException(e);
        }
    }


    // ---------------------- util ----------------------

    public Object getObject() {
        return Proxy.newProxyInstance(Thread.currentThread()
                        .getContextClassLoader(), new Class[]{iface},
                (proxy, method, args) -> {

                    // method param
                    String className = method.getDeclaringClass().getName();    // iface.getName()
                    String varsion_ = version;
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] parameters = args;

                    // filter for generic
                    if (className.equals(JobsRpcGenericService.class.getName()) && methodName.equals("invoke")) {

                        Class<?>[] paramTypes = null;
                        if (args[3] != null) {
                            String[] paramTypes_str = (String[]) args[3];
                            if (paramTypes_str.length > 0) {
                                paramTypes = new Class[paramTypes_str.length];
                                for (int i = 0; i < paramTypes_str.length; i++) {
                                    paramTypes[i] = ClassUtil.resolveClass(paramTypes_str[i]);
                                }
                            }
                        }

                        className = (String) args[0];
                        varsion_ = (String) args[1];
                        methodName = (String) args[2];
                        parameterTypes = paramTypes;
                        parameters = (Object[]) args[4];
                    }

                    // filter method like "Object.toString()"
                    if (className.equals(Object.class.getName())) {
                        log.info("Jobs rpc proxy class-method not support [{}#{}]", className, methodName);
                        throw new JobsRpcException("Jobs rpc proxy class-method not support");
                    }

                    // address
                    String finalAddress = address;
                    if (finalAddress == null || finalAddress.trim().length() == 0) {
                        if (invokerFactory != null && invokerFactory.getServiceRegistry() != null) {
                            // discovery
                            String serviceKey = JobsRpcProviderFactory.makeServiceKey(className, varsion_);
                            TreeSet<String> addressSet = invokerFactory.getServiceRegistry().discovery(serviceKey);
                            // load balance
                            if (addressSet == null || addressSet.size() == 0) {
                                // pass
                            } else if (addressSet.size() == 1) {
                                finalAddress = addressSet.first();
                            } else {
                                finalAddress = loadBalance.rpcLoadBalance.route(serviceKey, addressSet);
                            }

                        }
                    }
                    if (finalAddress == null || finalAddress.trim().length() == 0) {
                        throw new JobsRpcException("Jobs rpc reference bean[" + className + "] address empty");
                    }

                    // request
                    JobsRpcRequest jobsRpcRequest = new JobsRpcRequest();
                    jobsRpcRequest.setRequestId(UUID.randomUUID().toString());
                    jobsRpcRequest.setCreateMillisTime(System.currentTimeMillis());
                    jobsRpcRequest.setAccessToken(accessToken);
                    jobsRpcRequest.setClassName(className);
                    jobsRpcRequest.setMethodName(methodName);
                    jobsRpcRequest.setParameterTypes(parameterTypes);
                    jobsRpcRequest.setParameters(parameters);

                    // send
                    if (CallType.SYNC == callType) {
                        // future-response set
                        JobsRpcFutureResponse futureResponse = new JobsRpcFutureResponse(invokerFactory, jobsRpcRequest, null);
                        try {
                            // do invoke
                            client.asyncSend(finalAddress, jobsRpcRequest);

                            // future get
                            JobsRpcResponse xxlRpcResponse = futureResponse.get(timeout, TimeUnit.MILLISECONDS);
                            if (xxlRpcResponse.getErrorMsg() != null) {
                                throw new JobsRpcException(xxlRpcResponse.getErrorMsg());
                            }
                            return xxlRpcResponse.getResult();
                        } catch (Exception e) {
                            log.info("Jobs rpc, invoke error, address:{}, JobsRpcRequest{}", finalAddress, jobsRpcRequest);

                            throw (e instanceof JobsRpcException) ? e : new JobsRpcException(e);
                        } finally {
                            // future-response remove
                            futureResponse.removeInvokerFuture();
                        }
                    } else if (CallType.FUTURE == callType) {
                        // future-response set
                        JobsRpcFutureResponse futureResponse = new JobsRpcFutureResponse(invokerFactory, jobsRpcRequest, null);
                        try {
                            // invoke future set
                            JobsRpcInvokeFuture invokeFuture = new JobsRpcInvokeFuture(futureResponse);
                            JobsRpcInvokeFuture.setFuture(invokeFuture);

                            // do invoke
                            client.asyncSend(finalAddress, jobsRpcRequest);

                            return null;
                        } catch (Exception e) {
                            log.info("Jobs rpc, invoke error, address:{}, JobsRpcRequest{}", finalAddress, jobsRpcRequest);

                            // future-response remove
                            futureResponse.removeInvokerFuture();

                            throw (e instanceof JobsRpcException) ? e : new JobsRpcException(e);
                        }

                    } else if (CallType.CALLBACK == callType) {

                        // get callback
                        JobsRpcInvokeCallback finalInvokeCallback = invokeCallback;
                        JobsRpcInvokeCallback threadInvokeCallback = JobsRpcInvokeCallback.getCallback();
                        if (threadInvokeCallback != null) {
                            finalInvokeCallback = threadInvokeCallback;
                        }
                        if (finalInvokeCallback == null) {
                            throw new JobsRpcException("Jobs rpc JobsRpcInvokeCallback（CallType=" + CallType.CALLBACK.name() + "） cannot be null.");
                        }

                        // future-response set
                        JobsRpcFutureResponse futureResponse = new JobsRpcFutureResponse(invokerFactory, jobsRpcRequest, finalInvokeCallback);
                        try {
                            client.asyncSend(finalAddress, jobsRpcRequest);
                        } catch (Exception e) {
                            log.info("Jobs rpc, invoke error, address:{}, JobsRpcRequest{}", finalAddress, jobsRpcRequest);

                            // future-response remove
                            futureResponse.removeInvokerFuture();

                            throw (e instanceof JobsRpcException) ? e : new JobsRpcException(e);
                        }

                        return null;
                    } else if (CallType.ONEWAY == callType) {
                        client.asyncSend(finalAddress, jobsRpcRequest);
                        return null;
                    } else {
                        throw new JobsRpcException("Jobs rpc callType[" + callType + "] invalid");
                    }

                });
    }

    public Class<?> getObjectType() {
        return iface;
    }
}
