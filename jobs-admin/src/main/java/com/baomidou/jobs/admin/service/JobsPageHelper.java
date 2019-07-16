package com.baomidou.jobs.admin.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;

public class JobsPageHelper {

    /**
     * 分页信息
     */
    public static Page getPage(HttpServletRequest request) {
        long current = 1;
        long size = 10;
        String page = request.getParameter("page");
        if (StringUtils.isNotEmpty(page)) {
            Long p = Long.valueOf(page);
            if (p > 0) {
                current = p;
            }
        }
        String rows = request.getParameter("rows");
        if (StringUtils.isNotEmpty(page)) {
            Long l = Long.valueOf(rows);
            if (l > 0) {
                size = l;
            }
        }
        return new Page(current, size);
    }
}
