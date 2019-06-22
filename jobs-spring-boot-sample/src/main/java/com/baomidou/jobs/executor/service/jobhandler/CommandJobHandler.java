package com.baomidou.jobs.executor.service.jobhandler;

import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.handler.annotation.JobsHandler;
import com.baomidou.jobs.core.log.JobsLogger;
import com.baomidou.jobs.core.web.JobsResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 命令行任务
 *
 * @author xuxueli 2018-09-16 03:48:34
 */
@JobsHandler(value = "commandJobHandler")
@Component
public class CommandJobHandler extends IJobsHandler {

    @Override
    public JobsResponse<String> execute(String param) throws Exception {
        String command = param;
        int exitValue = -1;

        BufferedReader bufferedReader = null;
        try {
            // command process
            Process process = Runtime.getRuntime().exec(command);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JobsLogger.log(line);
            }

            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            JobsLogger.log(e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        if (exitValue == 0) {
            return JobsResponse.ok();
        } else {
            return JobsResponse.failed("command exit value(" + exitValue + ") is failed");
        }
    }

}
