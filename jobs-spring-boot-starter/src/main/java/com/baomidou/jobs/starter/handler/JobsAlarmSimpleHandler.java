package com.baomidou.jobs.starter.handler;

import com.baomidou.jobs.starter.model.JobsInfo;

public class JobsAlarmSimpleHandler implements IJobsAlarmHandler {

    @Override
    public boolean failed(JobsInfo jobInfo) {
        System.out.println(jobInfo.toString());
        return false;
    }
}
