package com.baomidou.jobs.starter.controller;

import com.baomidou.jobs.core.biz.AdminBiz;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.jobs.starter.starter.JobsScheduler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xuxueli on 17/5/10.
 */
@Controller
public class JobApiController implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @RequestMapping(AdminBiz.MAPPING)
    public void api(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        JobsScheduler.invokeAdminService(request, response);
    }
}
