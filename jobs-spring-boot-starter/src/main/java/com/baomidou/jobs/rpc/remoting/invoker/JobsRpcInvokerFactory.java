package com.baomidou.jobs.rpc.remoting.invoker;

import com.baomidou.jobs.rpc.registry.ServiceRegistry;
import com.baomidou.jobs.rpc.registry.impl.LocalServiceRegistry;
import com.baomidou.jobs.exception.JobsRpcException;
import com.baomidou.jobs.rpc.remoting.net.params.BaseCallback;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcFutureResponse;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Jobs rpc invoker factory, init service-registry
 *
 * @author xuxueli 2018-10-19
 */
public class JobsRpcInvokerFactory {
    private static Logger logger = LoggerFactory.getLogger(JobsRpcInvokerFactory.class);

    // ---------------------- default instance ----------------------

    private static volatile JobsRpcInvokerFactory instance = new JobsRpcInvokerFactory(LocalServiceRegistry.class, null);
    public static JobsRpcInvokerFactory getInstance() {
        return instance;
    }


    // ---------------------- config ----------------------

    private Class<? extends ServiceRegistry> serviceRegistryClass;
    private Map<String, String> serviceRegistryParam;


    public JobsRpcInvokerFactory() {
    }
    public JobsRpcInvokerFactory(Class<? extends ServiceRegistry> serviceRegistryClass, Map<String, String> serviceRegistryParam) {
        this.serviceRegistryClass = serviceRegistryClass;
        this.serviceRegistryParam = serviceRegistryParam;
    }


    // ---------------------- start / stop ----------------------

    public void start() throws Exception {
        // start registry
        if (serviceRegistryClass != null) {
            serviceRegistry = serviceRegistryClass.newInstance();
            serviceRegistry.start(serviceRegistryParam);
        }
    }

    public void  stop() throws Exception {
        // stop registry
        if (serviceRegistry != null) {
            serviceRegistry.stop();
        }

        // stop callback
        if (stopCallbackList.size() > 0) {
            for (BaseCallback callback: stopCallbackList) {
                try {
                    callback.run();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        // stop CallbackThreadPool
        stopCallbackThreadPool();
    }


    // ---------------------- service registry ----------------------

    private ServiceRegistry serviceRegistry;
    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }


    // ---------------------- service registry ----------------------

    private List<BaseCallback> stopCallbackList = new ArrayList<BaseCallback>();

    public void addStopCallBack(BaseCallback callback){
        stopCallbackList.add(callback);
    }


    // ---------------------- future-response pool ----------------------

    // XxlRpcFutureResponseFactory

    private ConcurrentMap<String, JobsRpcFutureResponse> futureResponsePool = new ConcurrentHashMap<String, JobsRpcFutureResponse>();
    public void setInvokerFuture(String requestId, JobsRpcFutureResponse futureResponse){
        futureResponsePool.put(requestId, futureResponse);
    }
    public void removeInvokerFuture(String requestId){
        futureResponsePool.remove(requestId);
    }
    public void notifyInvokerFuture(String requestId, final JobsRpcResponse xxlRpcResponse){

        // get
        final JobsRpcFutureResponse futureResponse = futureResponsePool.get(requestId);
        if (futureResponse == null) {
            return;
        }

        // notify
        if (futureResponse.getInvokeCallback()!=null) {

            // callback type
            try {
                executeResponseCallback(new Runnable() {
                    @Override
                    public void run() {
                        if (xxlRpcResponse.getErrorMsg() != null) {
                            futureResponse.getInvokeCallback().onFailure(new JobsRpcException(xxlRpcResponse.getErrorMsg()));
                        } else {
                            futureResponse.getInvokeCallback().onSuccess(xxlRpcResponse.getResult());
                        }
                    }
                });
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {

            // other nomal type
            futureResponse.setResponse(xxlRpcResponse);
        }

        // do remove
        futureResponsePool.remove(requestId);

    }


    // ---------------------- response callback ThreadPool ----------------------

    private ThreadPoolExecutor responseCallbackThreadPool = null;
    public void executeResponseCallback(Runnable runnable){

        if (responseCallbackThreadPool == null) {
            synchronized (this) {
                if (responseCallbackThreadPool == null) {
                    responseCallbackThreadPool = new ThreadPoolExecutor(
                            10,
                            100,
                            60L,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>(1000),
                            r -> new Thread(r, "Jobs rpc, JobsRpcInvokerFactory-responseCallbackThreadPool-" + r.hashCode()),
                            (r, executor) -> {
                                throw new JobsRpcException("Jobs rpc Invoke Callback Thread pool is EXHAUSTED!");
                            });		// default maxThreads 300, minThreads 60
                }
            }
        }
        responseCallbackThreadPool.execute(runnable);
    }

    public void stopCallbackThreadPool() {
        if (responseCallbackThreadPool != null) {
            responseCallbackThreadPool.shutdown();
        }
    }
}
