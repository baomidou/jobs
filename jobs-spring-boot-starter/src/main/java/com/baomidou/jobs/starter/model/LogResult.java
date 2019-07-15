package com.baomidou.jobs.starter.model;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author xuxueli
 * @date 17/3/23
 */
@Data
public class LogResult implements Serializable {
    private int fromLineNum;
    private int toLineNum;
    private String logContent;
    private boolean isEnd;

    public LogResult(int fromLineNum, int toLineNum, String logContent, boolean isEnd) {
        this.fromLineNum = fromLineNum;
        this.toLineNum = toLineNum;
        this.logContent = logContent;
        this.isEnd = isEnd;
    }
}
