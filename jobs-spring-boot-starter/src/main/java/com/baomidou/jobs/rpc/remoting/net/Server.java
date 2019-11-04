package com.baomidou.jobs.rpc.remoting.net;

import com.baomidou.jobs.exception.JobsRpcException;
import com.baomidou.jobs.rpc.remoting.net.params.IJobsRpcCallback;
import com.baomidou.jobs.rpc.remoting.provider.JobsRpcProviderFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * server
 *
 * @author xuxueli 2015-11-24 20:59:49
 */
@Slf4j
@Setter
public abstract class Server {
	private IJobsRpcCallback startedCallback;
	private IJobsRpcCallback stoppedCallback;

	/**
	 * start server
	 */
	public abstract void start(final JobsRpcProviderFactory jobsRpcProviderFactory) throws Exception;

	/**
	 * callback when started
	 */
	public void onStarted() {
		if (startedCallback != null) {
			try {
				startedCallback.execute();
			} catch (JobsRpcException e) {
				log.error("Jobs rpc, server startedCallback error.", e);
			}
		}
	}

	/**
	 * stop server
	 *
	 * @throws Exception
	 */
	public abstract void stop() throws Exception;

	/**
	 * callback when stopped
	 */
	public void onStopped() {
		if (null != stoppedCallback) {
			try {
				stoppedCallback.execute();
			} catch (JobsRpcException e) {
				log.error("Jobs rpc, server stoppedCallback error.", e);
			}
		}
	}
}
