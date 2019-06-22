package com.baomidou.jobs.core.thread;

import com.baomidou.jobs.core.util.FileUtil;
import com.baomidou.jobs.core.log.JobsFileAppender;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * job file clean thread
 *
 * @author xuxueli 2017-12-29 16:23:43
 */
@Slf4j
public class JobsLogFileCleanThread {
    private static JobsLogFileCleanThread instance = new JobsLogFileCleanThread();
    public static JobsLogFileCleanThread getInstance(){
        return instance;
    }

    private Thread localThread;
    private volatile boolean toStop = false;
    public void start(final long logRetentionDays){

        // limit min value
        if (logRetentionDays < 3 ) {
            return;
        }

        localThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!toStop) {
                    try {
                        // clean log dir, over logRetentionDays
                        File[] childDirs = new File(JobsFileAppender.getLogPath()).listFiles();
                        if (childDirs!=null && childDirs.length>0) {

                            // today
                            Calendar todayCal = Calendar.getInstance();
                            todayCal.set(Calendar.HOUR_OF_DAY,0);
                            todayCal.set(Calendar.MINUTE,0);
                            todayCal.set(Calendar.SECOND,0);
                            todayCal.set(Calendar.MILLISECOND,0);

                            Date todayDate = todayCal.getTime();

                            for (File childFile: childDirs) {

                                // valid
                                if (!childFile.isDirectory()) {
                                    continue;
                                }
                                if (childFile.getName().indexOf("-") == -1) {
                                    continue;
                                }

                                // file create date
                                Date logFileCreateDate = null;
                                try {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    logFileCreateDate = simpleDateFormat.parse(childFile.getName());
                                } catch (ParseException e) {
                                    log.error(e.getMessage(), e);
                                }
                                if (logFileCreateDate == null) {
                                    continue;
                                }

                                if ((todayDate.getTime()-logFileCreateDate.getTime()) >= logRetentionDays * (24 * 60 * 60 * 1000) ) {
                                    FileUtil.deleteRecursively(childFile);
                                }

                            }
                        }

                    } catch (Exception e) {
                        if (!toStop) {
                            log.error(e.getMessage(), e);
                        }

                    }

                    try {
                        TimeUnit.DAYS.sleep(1);
                    } catch (InterruptedException e) {
                        if (!toStop) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
                log.info(">>>>>>>>>>> jobs, executor JobsLogFileCleanThread thread destory.");

            }
        });
        localThread.setDaemon(true);
        localThread.setName("jobs, executor JobsLogFileCleanThread");
        localThread.start();
    }

    public void toStop() {
        toStop = true;

        if (localThread == null) {
            return;
        }

        // interrupt and wait
        localThread.interrupt();
        try {
            localThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
