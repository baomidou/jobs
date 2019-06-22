package com.baomidou.jobs.starter.controller;

import com.baomidou.jobs.core.web.IJobsAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.jobs.starter.starter.JobsScheduler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jobs Api
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
@Controller
public class JobApiController implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @RequestMapping(IJobsAdmin.MAPPING)
    public void api(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        JobsScheduler.invokeAdminService(request, response);
    }
}
