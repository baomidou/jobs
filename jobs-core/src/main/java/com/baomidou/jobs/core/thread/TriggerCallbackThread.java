package com.baomidou.jobs.core.thread;

import com.baomidou.jobs.core.JobsConstant;
import com.baomidou.jobs.core.enums.RegistryConfig;
import com.baomidou.jobs.core.executor.JobsAbstractExecutor;
import com.baomidou.jobs.core.log.JobsFileAppender;
import com.baomidou.jobs.core.log.JobsLogger;
import com.baomidou.jobs.core.model.HandleCallbackParam;
import com.baomidou.jobs.core.util.FileUtil;
import com.baomidou.jobs.core.web.IJobsAdmin;
import com.baomidou.jobs.core.web.JobsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuxueli on 16/7/22.
 */
public class TriggerCallbackThread {
    private static Logger logger = LoggerFactory.getLogger(TriggerCallbackThread.class);

    private static TriggerCallbackThread instance = new TriggerCallbackThread();
    public static TriggerCallbackThread getInstance(){
        return instance;
    }

    /**
     * job results callback queue
     */
    private LinkedBlockingQueue<HandleCallbackParam> callBackQueue = new LinkedBlockingQueue<HandleCallbackParam>();
    public static void pushCallBack(HandleCallbackParam callback){
        getInstance().callBackQueue.add(callback);
        logger.debug(">>>>>>>>>>> jobs, push callback request, logId:{}", callback.getLogId());
    }

    /**
     * callback thread
     */
    private Thread triggerCallbackThread;
    private Thread triggerRetryCallbackThread;
    private volatile boolean toStop = false;
    public void start() {

        // valid
        if (JobsAbstractExecutor.getAdminBizList() == null) {
            logger.warn(">>>>>>>>>>> jobs, executor callback config fail, adminAddresses is null.");
            return;
        }

        // callback
        triggerCallbackThread = new Thread(new Runnable() {

            @Override
            public void run() {

                // normal callback
                while(!toStop){
                    try {
                        HandleCallbackParam callback = getInstance().callBackQueue.take();
                        if (callback != null) {

                            // callback list param
                            List<HandleCallbackParam> callbackParamList = new ArrayList<HandleCallbackParam>();
                            int drainToNum = getInstance().callBackQueue.drainTo(callbackParamList);
                            callbackParamList.add(callback);

                            // callback, will retry if error
                            if (callbackParamList!=null && callbackParamList.size()>0) {
                                doCallback(callbackParamList);
                            }
                        }
                    } catch (Exception e) {
                        if (!toStop) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }

                // last callback
                try {
                    List<HandleCallbackParam> callbackParamList = new ArrayList<HandleCallbackParam>();
                    int drainToNum = getInstance().callBackQueue.drainTo(callbackParamList);
                    if (callbackParamList!=null && callbackParamList.size()>0) {
                        doCallback(callbackParamList);
                    }
                } catch (Exception e) {
                    if (!toStop) {
                        logger.error(e.getMessage(), e);
                    }
                }
                logger.info(">>>>>>>>>>> jobs, executor callback thread destory.");

            }
        });
        triggerCallbackThread.setDaemon(true);
        triggerCallbackThread.setName("jobs, executor TriggerCallbackThread");
        triggerCallbackThread.start();


        // retry
        triggerRetryCallbackThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!toStop){
                    try {
                        retryFailCallbackFile();
                    } catch (Exception e) {
                        if (!toStop) {
                            logger.error(e.getMessage(), e);
                        }

                    }
                    try {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                    } catch (InterruptedException e) {
                        if (!toStop) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
                logger.info(">>>>>>>>>>> jobs, executor retry callback thread destory.");
            }
        });
        triggerRetryCallbackThread.setDaemon(true);
        triggerRetryCallbackThread.start();

    }
    public void toStop(){
        toStop = true;
        // stop callback, interrupt and wait
        triggerCallbackThread.interrupt();
        try {
            triggerCallbackThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        // stop retry, interrupt and wait
        triggerRetryCallbackThread.interrupt();
        try {
            triggerRetryCallbackThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * do callback, will retry if error
     * @param callbackParamList
     */
    private void doCallback(List<HandleCallbackParam> callbackParamList){
        boolean callbackRet = false;
        // callback, will retry if error
        for (IJobsAdmin adminBiz: JobsAbstractExecutor.getAdminBizList()) {
            try {
                JobsResponse<String> callbackResult = adminBiz.callback(callbackParamList);
                if (callbackResult!=null && JobsConstant.CODE_SUCCESS == callbackResult.getCode()) {
                    callbackLog(callbackParamList, "<br>----------- jobs job callback finish.");
                    callbackRet = true;
                    break;
                } else {
                    callbackLog(callbackParamList, "<br>----------- jobs job callback fail, callbackResult:" + callbackResult);
                }
            } catch (Exception e) {
                callbackLog(callbackParamList, "<br>----------- jobs job callback error, errorMsg:" + e.getMessage());
            }
        }
        if (!callbackRet) {
            appendFailCallbackFile(callbackParamList);
        }
    }

    /**
     * callback log
     */
    private void callbackLog(List<HandleCallbackParam> callbackParamList, String logContent){
        for (HandleCallbackParam callbackParam: callbackParamList) {
            String logFileName = JobsFileAppender.makeLogFileName(new Date(callbackParam.getLogDateTim()), callbackParam.getLogId());
            JobsFileAppender.contextHolder.set(logFileName);
            JobsLogger.log(logContent);
        }
    }


    // ---------------------- fail-callback file ----------------------

    private static String failCallbackFilePath = JobsFileAppender.getLogPath().concat(File.separator).concat("callbacklog").concat(File.separator);
    private static String failCallbackFileName = failCallbackFilePath.concat("jobs-callback-{x}").concat(".log");

    private void appendFailCallbackFile(List<HandleCallbackParam> callbackParamList){
        // valid
        if (callbackParamList==null || callbackParamList.size()==0) {
            return;
        }

        // append file
        byte[] callbackParamList_bytes = JobsAbstractExecutor.getSerializer().serialize(callbackParamList);

        File callbackLogFile = new File(failCallbackFileName.replace("{x}", String.valueOf(System.currentTimeMillis())));
        if (callbackLogFile.exists()) {
            for (int i = 0; i < 100; i++) {
                callbackLogFile = new File(failCallbackFileName.replace("{x}", String.valueOf(System.currentTimeMillis()).concat("-").concat(String.valueOf(i)) ));
                if (!callbackLogFile.exists()) {
                    break;
                }
            }
        }
        FileUtil.writeFileContent(callbackLogFile, callbackParamList_bytes);
    }

    private void retryFailCallbackFile(){

        // valid
        File callbackLogPath = new File(failCallbackFilePath);
        if (!callbackLogPath.exists()) {
            return;
        }
        if (callbackLogPath.isFile()) {
            callbackLogPath.delete();
        }
        if (!(callbackLogPath.isDirectory() && callbackLogPath.list()!=null && callbackLogPath.list().length>0)) {
            return;
        }

        // load and clear file, retry
        for (File callbaclLogFile: callbackLogPath.listFiles()) {
            byte[] callbackParamList_bytes = FileUtil.readFileContent(callbaclLogFile);
            List<HandleCallbackParam> callbackParamList = (List<HandleCallbackParam>) JobsAbstractExecutor.getSerializer().deserialize(callbackParamList_bytes, HandleCallbackParam.class);

            callbaclLogFile.delete();
            doCallback(callbackParamList);
        }

    }

}
