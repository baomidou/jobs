package com.baomidou.jobs.rpc.remoting.net.params;

import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.remoting.invoker.call.JobsRpcInvokeCallback;
import com.baomidou.jobs.exception.JobsRpcException;

import java.util.concurrent.*;

/**
 * call back future
 *
 * @author xuxueli 2015-11-5 14:26:37
 */
public class JobsRpcFutureResponse implements Future<JobsRpcResponse> {

    private JobsRpcInvokerFactory invokerFactory;

    /**
     * net data
     */
    private JobsRpcRequest request;
    private JobsRpcResponse response;

    /**
     * future lock
     */
    private boolean done = false;
    private Object lock = new Object();

    /**
     * callback, can be null
     */
    private JobsRpcInvokeCallback invokeCallback;


    public JobsRpcFutureResponse(final JobsRpcInvokerFactory invokerFactory, JobsRpcRequest request, JobsRpcInvokeCallback invokeCallback) {
        this.invokerFactory = invokerFactory;
        this.request = request;
        this.invokeCallback = invokeCallback;

        // set-InvokerFuture
        setInvokerFuture();
    }


    // ---------------------- response pool ----------------------

    public void setInvokerFuture() {
        this.invokerFactory.setInvokerFuture(request.getRequestId(), this);
    }

    public void removeInvokerFuture() {
        this.invokerFactory.removeInvokerFuture(request.getRequestId());
    }


    // ---------------------- get ----------------------

    public JobsRpcRequest getRequest() {
        return request;
    }

    public JobsRpcInvokeCallback getInvokeCallback() {
        return invokeCallback;
    }


    // ---------------------- for invoke back ----------------------

    public void setResponse(JobsRpcResponse response) {
        this.response = response;
        synchronized (lock) {
            done = true;
            lock.notifyAll();
        }
    }


    // ---------------------- for invoke ----------------------

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // TODO
        return false;
    }

    @Override
    public boolean isCancelled() {
        // TODO
        return false;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public JobsRpcResponse get() throws InterruptedException, ExecutionException {
        try {
            return get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new JobsRpcException(e);
        }
    }

    @Override
    public JobsRpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!done) {
            synchronized (lock) {
                try {
                    if (timeout < 0) {
                        lock.wait();
                    } else {
                        long timeoutMillis = (TimeUnit.MILLISECONDS == unit) ? timeout : TimeUnit.MILLISECONDS.convert(timeout, unit);
                        lock.wait(timeoutMillis);
                    }
                } catch (InterruptedException e) {
                    throw e;
                }
            }
        }

        if (!done) {
            throw new JobsRpcException("Jobs rpc, request timeout at:" + System.currentTimeMillis() + ", request:" + request.toString());
        }
        return response;
    }
}
