package com.baomidou.jobs.core.thread;

import com.baomidou.jobs.core.executor.JobsAbstractExecutor;
import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.model.HandleCallbackParam;
import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.web.JobsResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * handler thread
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
@Slf4j
public class JobsThread extends Thread {
    private Long jobId;
    private IJobsHandler handler;
    private LinkedBlockingQueue<TriggerParam> triggerQueue;
    /**
     * avoid repeat trigger for the same TRIGGER_LOG_ID
     */
    private Set<Long> triggerLogIdSet;
    private volatile boolean toStop = false;
    private String stopReason;
    private boolean running = false;
    private int idleTimes = 0;


    public JobsThread(Long jobId, IJobsHandler handler) {
        this.jobId = jobId;
        this.handler = handler;
        this.triggerQueue = new LinkedBlockingQueue<>();
        this.triggerLogIdSet = Collections.synchronizedSet(new HashSet<>());
    }

    public IJobsHandler getHandler() {
        return handler;
    }

    /**
     * new trigger to queue
     *
     * @param triggerParam
     * @return
     */
    public JobsResponse<String> pushTriggerQueue(TriggerParam triggerParam) {
        // avoid repeat
        if (triggerLogIdSet.contains(triggerParam.getLogId())) {
            log.info("Repeat trigger job, logId:{}", triggerParam.getLogId());
            return JobsResponse.failed("repeate trigger job, logId:" + triggerParam.getLogId());
        }

        triggerLogIdSet.add(triggerParam.getLogId());
        triggerQueue.add(triggerParam);
        return JobsResponse.ok();
    }

    /**
     * kill job thread
     *
     * @param stopReason
     */
    public void toStop(String stopReason) {
        /**
         * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
         * 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身；
         * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
         */
        this.toStop = true;
        this.stopReason = stopReason;
    }

    /**
     * is running job
     *
     * @return
     */
    public boolean isRunningOrHasQueue() {
        return running || triggerQueue.size() > 0;
    }

    @Override
    public void run() {

        // init
        try {
            handler.init();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        // execute
        while (!toStop) {
            running = false;
            idleTimes++;

            TriggerParam triggerParam = null;
            JobsResponse<String> executeResult = null;
            try {
                // to check toStop signal, we need cycle, so wo cannot use queue.take(), instand of poll(timeout)
                triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
                if (triggerParam != null) {
                    running = true;
                    idleTimes = 0;
                    triggerLogIdSet.remove(triggerParam.getLogId());

                    // execute
                    log.debug("Jobs job execute param:" + triggerParam.getExecutorParams());

                    if (triggerParam.getExecutorTimeout() > 0) {
                        // limit timeout
                        Thread futureThread = null;
                        try {
                            final TriggerParam triggerParamTmp = triggerParam;
                            FutureTask<JobsResponse<String>> futureTask = new FutureTask<>(() ->
                                    handler.execute(triggerParamTmp.getExecutorParams()));
                            futureThread = new Thread(futureTask);
                            futureThread.start();

                            executeResult = futureTask.get(triggerParam.getExecutorTimeout(), TimeUnit.SECONDS);
                        } catch (TimeoutException e) {
                            log.error("Jobs execute timeout", e);
                            executeResult = JobsResponse.failed("job execute timeout ");
                        } finally {
                            if (null != futureThread) {
                                futureThread.interrupt();
                            }
                        }
                    } else {
                        // just execute
                        executeResult = handler.execute(triggerParam.getExecutorParams());
                    }

                    if (executeResult == null) {
                        executeResult = JobsResponse.failed("execute result is null");
                    } else {
                        executeResult.setMsg(
                                (executeResult != null && executeResult.getMsg() != null && executeResult.getMsg().length() > 50000)
                                        ? executeResult.getMsg().substring(0, 50000).concat("...")
                                        : executeResult.getMsg());
                    }
                    log.debug("Jobs execute end(finish) JobsResponse:{}", executeResult);

                } else {
                    if (idleTimes > 30) {
                        JobsAbstractExecutor.removeJobsThread(jobId, "excutor idel times over limit.");
                    }
                }
            } catch (Throwable e) {
                if (toStop) {
                    log.debug("JobsThread toStop, stopReason:{}", stopReason);
                }

                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                String errorMsg = stringWriter.toString();
                executeResult = JobsResponse.failed(errorMsg);

                log.debug("JobsThread Exception:{} , executeResult:{}", errorMsg, executeResult);
            } finally {
                if (triggerParam != null) {
                    // callback handler info
                    if (!toStop) {
                        // commonm
                        TriggerCallbackThread.pushCallBack(new HandleCallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTime(), executeResult));
                    } else {
                        // is killed
                        JobsResponse<String> stopResult = JobsResponse.failed(stopReason + " [job running，killed]");
                        TriggerCallbackThread.pushCallBack(new HandleCallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTime(), stopResult));
                    }
                }
            }
        }

        // callback trigger request in queue
        while (triggerQueue != null && triggerQueue.size() > 0) {
            TriggerParam triggerParam = triggerQueue.poll();
            if (triggerParam != null) {
                // is killed
                JobsResponse<String> stopResult = JobsResponse.failed(stopReason + " [job not executed, in the job queue, killed.]");
                TriggerCallbackThread.pushCallBack(new HandleCallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTime(), stopResult));
            }
        }

        // destroy
        try {
            handler.destroy();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        log.info("Jobs JobsThread stoped, hashCode:{}", Thread.currentThread());
    }
}
