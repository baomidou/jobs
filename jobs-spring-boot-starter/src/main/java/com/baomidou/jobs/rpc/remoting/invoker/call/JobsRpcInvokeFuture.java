package com.baomidou.jobs.rpc.remoting.invoker.call;

import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcFutureResponse;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import com.baomidou.jobs.exception.JobsRpcException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xuxueli 2018-10-22 18:31:42
 */
public class JobsRpcInvokeFuture implements Future {


    private JobsRpcFutureResponse futureResponse;

    public JobsRpcInvokeFuture(JobsRpcFutureResponse futureResponse) {
        this.futureResponse = futureResponse;
    }

    public void stop() {
        // remove-InvokerFuture
        futureResponse.removeInvokerFuture();
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return futureResponse.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return futureResponse.isCancelled();
    }

    @Override
    public boolean isDone() {
        return futureResponse.isDone();
    }

    @Override
    public Object get() throws ExecutionException, InterruptedException {
        try {
            return get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new JobsRpcException(e);
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            // future get
            JobsRpcResponse xxlRpcResponse = futureResponse.get(timeout, unit);
            if (xxlRpcResponse.getErrorMsg() != null) {
                throw new JobsRpcException(xxlRpcResponse.getErrorMsg());
            }
            return xxlRpcResponse.getResult();
        } finally {
            stop();
        }
    }


    // ---------------------- thread invoke future ----------------------

    private static ThreadLocal<JobsRpcInvokeFuture> threadInvokerFuture = new ThreadLocal<JobsRpcInvokeFuture>();

    /**
     * get future
     */
    public static <T> Future<T> getFuture(Class<T> type) {
        Future<T> future = (Future<T>) threadInvokerFuture.get();
        threadInvokerFuture.remove();
        return future;
    }

    /**
     * set future
     */
    public static void setFuture(JobsRpcInvokeFuture future) {
        threadInvokerFuture.set(future);
    }

    /**
     * remove future
     */
    public static void removeFuture() {
        threadInvokerFuture.remove();
    }

}

