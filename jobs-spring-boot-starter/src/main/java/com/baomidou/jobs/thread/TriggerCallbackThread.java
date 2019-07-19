package com.baomidou.jobs.thread;

import com.baomidou.jobs.JobsConstant;
import com.baomidou.jobs.executor.JobsAbstractExecutor;
import com.baomidou.jobs.model.param.HandleCallbackParam;
import com.baomidou.jobs.service.IJobsService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 触发器回调线程
 *
 * @author xxl jobob
 * @since 2019-06-23
 */
@Slf4j
public class TriggerCallbackThread {
    private static TriggerCallbackThread instance = new TriggerCallbackThread();

    public static TriggerCallbackThread getInstance() {
        return instance;
    }

    /**
     * job results callback queue
     */
    private LinkedBlockingQueue<HandleCallbackParam> callBackQueue = new LinkedBlockingQueue<HandleCallbackParam>();

    public static void pushCallBack(HandleCallbackParam callback) {
        getInstance().callBackQueue.add(callback);
        log.debug("Jobs push callback request, logId:{}", callback.getLogId());
    }

    /**
     * callback thread
     */
    private Thread triggerCallbackThread;
    private Thread triggerRetryCallbackThread;
    private volatile boolean toStop = false;

    public void start() {

        // valid
        if (null == JobsAbstractExecutor.getJobsServiceList()) {
            log.warn("Jobs executor callback config fail, adminAddresses is null.");
            return;
        }

        // callback
        triggerCallbackThread = new Thread(() -> {

            // normal callback
            while (!toStop) {
                try {
                    HandleCallbackParam callback = getInstance().callBackQueue.take();
                    if (callback != null) {

                        // callback list param
                        List<HandleCallbackParam> callbackParamList = new ArrayList<>();
                        int drainToNum = getInstance().callBackQueue.drainTo(callbackParamList);
                        callbackParamList.add(callback);

                        // callback, will retry if error
                        if (callbackParamList != null && callbackParamList.size() > 0) {
                            doCallback(callbackParamList);
                        }
                    }
                } catch (Exception e) {
                    if (!toStop) {
                        log.error(e.getMessage(), e);
                    }
                }
            }

            // last callback
            try {
                List<HandleCallbackParam> callbackParamList = new ArrayList<HandleCallbackParam>();
                int drainToNum = getInstance().callBackQueue.drainTo(callbackParamList);
                if (callbackParamList != null && callbackParamList.size() > 0) {
                    doCallback(callbackParamList);
                }
            } catch (Exception e) {
                if (!toStop) {
                    log.error(e.getMessage(), e);
                }
            }
            log.info("Jobs executor callback thread destory.");

        });
        triggerCallbackThread.setDaemon(true);
        triggerCallbackThread.setName("jobs, executor TriggerCallbackThread");
        triggerCallbackThread.start();


        // retry
        triggerRetryCallbackThread = new Thread(() -> {
            while (!toStop) {
                try {
                    TimeUnit.SECONDS.sleep(JobsConstant.BEAT_TIMEOUT);
                } catch (InterruptedException e) {
                    if (!toStop) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            log.info("Jobs executor retry callback thread destory.");
        });
        triggerRetryCallbackThread.setDaemon(true);
        triggerRetryCallbackThread.start();

    }

    public void toStop() {
        toStop = true;
        // stop callback, interrupt and wait
        triggerCallbackThread.interrupt();
        try {
            triggerCallbackThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        // stop retry, interrupt and wait
        triggerRetryCallbackThread.interrupt();
        try {
            triggerRetryCallbackThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * do callback, will retry if error
     *
     * @param callbackParamList
     */
    private void doCallback(List<HandleCallbackParam> callbackParamList) {
        // callback, will retry if error
        for (IJobsService jobsService : JobsAbstractExecutor.getJobsServiceList()) {
            try {
                boolean callbackResult = jobsService.callback(callbackParamList);
                log.debug("doCallback:{}", callbackResult);
            } catch (Exception e) {
                log.error("doCallback error", e);
            }
        }
    }
}
