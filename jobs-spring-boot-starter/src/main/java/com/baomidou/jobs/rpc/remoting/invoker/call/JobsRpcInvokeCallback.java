package com.baomidou.jobs.rpc.remoting.invoker.call;


/**
 * @author xuxueli 2018-10-23
 */
public abstract class JobsRpcInvokeCallback<T> {

    public abstract void onSuccess(T result);

    public abstract void onFailure(Throwable exception);


    // ---------------------- thread invoke callback ----------------------

    private static ThreadLocal<JobsRpcInvokeCallback> threadInvokerFuture = new ThreadLocal<JobsRpcInvokeCallback>();

    /**
     * get callback
     *
     * @return JobsRpcInvokeCallback
     */
    public static JobsRpcInvokeCallback getCallback() {
        JobsRpcInvokeCallback invokeCallback = threadInvokerFuture.get();
        threadInvokerFuture.remove();
        return invokeCallback;
    }

    /**
     * set future
     */
    public static void setCallback(JobsRpcInvokeCallback invokeCallback) {
        threadInvokerFuture.set(invokeCallback);
    }

    /**
     * remove future
     */
    public static void removeCallback() {
        threadInvokerFuture.remove();
    }


}
