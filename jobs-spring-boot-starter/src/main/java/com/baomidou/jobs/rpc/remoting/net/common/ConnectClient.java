package com.baomidou.jobs.rpc.remoting.net.common;

import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.remoting.invoker.reference.JobsRpcReferenceBean;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xuxueli 2018-10-19
 */
@Slf4j
public abstract class ConnectClient {

    public abstract void init(String address, final IJobsRpcSerializer serializer, final JobsRpcInvokerFactory xxlRpcInvokerFactory) throws Exception;

    public abstract void close();

    public abstract boolean isValidate();

    public abstract void send(JobsRpcRequest jobsRpcRequest) throws Exception;

    /**
     * async send
     */
    public static void asyncSend(JobsRpcRequest xxlRpcRequest, String address,
                                 Class<? extends ConnectClient> connectClientImpl,
                                 final JobsRpcReferenceBean xxlRpcReferenceBean) throws Exception {

        // client pool	[tips03 : may save 35ms/100invoke if move it to constructor, but it is necessary. cause by ConcurrentHashMap.get]
        ConnectClient clientPool = ConnectClient.getPool(address, connectClientImpl, xxlRpcReferenceBean);

        try {
            // do invoke
            clientPool.send(xxlRpcRequest);
        } catch (Exception e) {
            throw e;
        }
    }

    private static volatile ConcurrentHashMap<String, ConnectClient> connectClientMap;
    private static volatile ConcurrentHashMap<String, Object> connectClientLockMap = new ConcurrentHashMap<>();

    private static ConnectClient getPool(String address, Class<? extends ConnectClient> connectClientImpl,
                                         final JobsRpcReferenceBean jobsRpcReferenceBean) throws Exception {

        // init base compont, avoid repeat init
        if (connectClientMap == null) {
            synchronized (ConnectClient.class) {
                if (connectClientMap == null) {
                    // init
                    connectClientMap = new ConcurrentHashMap<>(16);
                    // stop callback
                    jobsRpcReferenceBean.getInvokerFactory().addStopCallBack(() -> {
                        if (connectClientMap.size() > 0) {
                            for (String key : connectClientMap.keySet()) {
                                ConnectClient clientPool = connectClientMap.get(key);
                                clientPool.close();
                            }
                            connectClientMap.clear();
                        }
                    });
                }
            }
        }

        // get-valid client
        ConnectClient connectClient = connectClientMap.get(address);
        if (connectClient != null && connectClient.isValidate()) {
            return connectClient;
        }

        // lock
        Object clientLock = connectClientLockMap.get(address);
        if (clientLock == null) {
            connectClientLockMap.putIfAbsent(address, new Object());
            clientLock = connectClientLockMap.get(address);
        }

        // remove-create new client
        synchronized (clientLock) {

            // get-valid client, avlid repeat
            connectClient = connectClientMap.get(address);
            if (connectClient != null && connectClient.isValidate()) {
                return connectClient;
            }

            // remove old
            if (connectClient != null) {
                connectClient.close();
                connectClientMap.remove(address);
            }

            // set pool
            ConnectClient connectClient_new = connectClientImpl.newInstance();
            try {
                connectClient_new.init(address, jobsRpcReferenceBean.getSerializer(), jobsRpcReferenceBean.getInvokerFactory());
                connectClientMap.put(address, connectClient_new);
            } catch (Exception e) {
                connectClient_new.close();
                throw e;
            }

            return connectClient_new;
        }
    }
}
