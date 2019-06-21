package xxl.job.web.handler;

import xxl.job.web.entity.XxlJobInfo;

public class XxlJobAlarmSimpleHandler implements IXxlJobAlarmHandler {

    @Override
    public boolean failed(XxlJobInfo jobInfo) {
        System.out.println(jobInfo.toString());
        return false;
    }
}
