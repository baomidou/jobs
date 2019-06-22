package com.baomidou.jobs.core.handler;

import com.baomidou.jobs.core.web.JobsResponse;

/**
 * job handler
 *
 * @author xuxueli 2015-12-19 19:06:38
 */
public abstract class IJobsHandler {


	/**
	 * execute handler, invoked when executor receives a scheduling request
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public abstract JobsResponse<String> execute(String param) throws Exception;


	/**
	 * init handler, invoked when JobsThread init
	 */
	public void init() {
		// do something
	}


	/**
	 * destroy handler, invoked when JobsThread destroy
	 */
	public void destroy() {
		// do something
	}
}
