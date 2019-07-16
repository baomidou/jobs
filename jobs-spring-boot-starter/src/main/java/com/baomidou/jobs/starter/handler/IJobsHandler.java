package com.baomidou.jobs.starter.handler;

import com.baomidou.jobs.starter.api.JobsResponse;

/**
 * job handler interface
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
public interface IJobsHandler {


	/**
	 * execute handler, invoked when executor receives a scheduling request
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	JobsResponse<String> execute(String param) throws Exception;


	/**
	 * init handler, invoked when JobsThread init
	 */
	default void init() {
		// do something
	}


	/**
	 * destroy handler, invoked when JobsThread destroy
	 */
	default void destroy() {
		// do something
	}
}
