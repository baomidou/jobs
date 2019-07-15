package com.baomidou.jobs.admin.controller;

import com.baomidou.jobs.starter.web.IJobsErrorCode;
import com.baomidou.jobs.starter.web.JobsResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service 控制层父类
 *
 * @author xxl jobob
 * @since 2019-05-31
 */
public class BaseController {
    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;


    /**
     * 请求成功
     *
     * @param data 数据内容
     * @param <T>  对象泛型
     * @return ignore
     */
    protected <T> JobsResponse<T> success(T data) {
        return JobsResponse.ok(data);
    }

    /**
     * 请求失败
     *
     * @param msg 提示内容
     * @return ignore
     */
    protected <T> JobsResponse<T> failed(String msg) {
        return JobsResponse.failed(msg);
    }

    /**
     * 请求失败
     *
     * @param errorCode 请求错误码
     * @return ignore
     */
    protected <T> JobsResponse<T> failed(IJobsErrorCode errorCode) {
        return JobsResponse.failed(errorCode);
    }

}
