package com.baomidou.jobs.admin.controller;

import com.baomidou.mybatisplus.extension.api.R;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service 控制层父类
 *
 * @author jobob
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
    protected <T> R<T> success(T data) {
        return R.ok(data);
    }

    /**
     * 请求失败
     *
     * @param msg 提示内容
     * @return ignore
     */
    protected <T> R<T> failed(String msg) {
        return R.failed(msg);
    }

}
