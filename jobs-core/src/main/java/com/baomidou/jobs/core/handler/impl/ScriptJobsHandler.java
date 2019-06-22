package com.baomidou.jobs.core.handler.impl;

import com.baomidou.jobs.core.glue.GlueTypeEnum;
import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.log.JobsFileAppender;
import com.baomidou.jobs.core.log.JobsLogger;
import com.baomidou.jobs.core.util.ScriptUtil;
import com.baomidou.jobs.core.util.ShardingUtil;
import com.baomidou.jobs.core.web.JobsResponse;

import java.io.File;

/**
 * Created by xuxueli on 17/4/27.
 */
public class ScriptJobsHandler implements IJobsHandler {

    private int jobId;
    private long glueUpdatetime;
    private String gluesource;
    private GlueTypeEnum glueType;

    public ScriptJobsHandler(int jobId, long glueUpdatetime, String gluesource, GlueTypeEnum glueType) {
        this.jobId = jobId;
        this.glueUpdatetime = glueUpdatetime;
        this.gluesource = gluesource;
        this.glueType = glueType;

        // clean old script file
        File glueSrcPath = new File(JobsFileAppender.getGlueSrcPath());
        if (glueSrcPath.exists()) {
            File[] glueSrcFileList = glueSrcPath.listFiles();
            if (glueSrcFileList != null && glueSrcFileList.length > 0) {
                for (File glueSrcFileItem : glueSrcFileList) {
                    if (glueSrcFileItem.getName().startsWith(String.valueOf(jobId) + "_")) {
                        glueSrcFileItem.delete();
                    }
                }
            }
        }

    }

    public long getGlueUpdatetime() {
        return glueUpdatetime;
    }

    @Override
    public JobsResponse<String> execute(String param) throws Exception {

        if (!glueType.isScript()) {
            return JobsResponse.failed("glueType[" + glueType + "] invalid.");
        }

        // cmd
        String cmd = glueType.getCmd();

        // make script file
        String scriptFileName = JobsFileAppender.getGlueSrcPath()
                .concat(File.separator)
                .concat(String.valueOf(jobId))
                .concat("_")
                .concat(String.valueOf(glueUpdatetime))
                .concat(glueType.getSuffix());
        File scriptFile = new File(scriptFileName);
        if (!scriptFile.exists()) {
            ScriptUtil.markScriptFile(scriptFileName, gluesource);
        }

        // log file
        String logFileName = JobsFileAppender.contextHolder.get();

        // script params：0=param、1=分片序号、2=分片总数
        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
        String[] scriptParams = new String[3];
        scriptParams[0] = param;
        scriptParams[1] = String.valueOf(shardingVO.getIndex());
        scriptParams[2] = String.valueOf(shardingVO.getTotal());

        // invoke
        JobsLogger.log("----------- script file:" + scriptFileName + " -----------");
        int exitValue = ScriptUtil.execToFile(cmd, scriptFileName, logFileName, scriptParams);

        return exitValue == 0 ? JobsResponse.ok() :
                JobsResponse.failed("script exit value(" + exitValue + ") is failed");
    }

}
