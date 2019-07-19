package com.baomidou.jobs.rpc.remoting.net;

import com.baomidou.jobs.rpc.remoting.net.params.BaseCallback;
import com.baomidou.jobs.rpc.remoting.provider.JobsRpcProviderFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * server
 *
 * @author xuxueli 2015-11-24 20:59:49
 */
@Slf4j
@Setter
public abstract class Server {
	private BaseCallback startedCallback;
	private BaseCallback stopedCallback;

	/**
	 * start server
	 *
	 * @param xxlRpcProviderFactory
	 * @throws Exception
	 */
	public abstract void start(final JobsRpcProviderFactory xxlRpcProviderFactory) throws Exception;

	/**
	 * callback when started
	 */
	public void onStarted() {
		if (startedCallback != null) {
			try {
				startedCallback.run();
			} catch (Exception e) {
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
	 * callback when stoped
	 */
	public void onStoped() {
		if (stopedCallback != null) {
			try {
				stopedCallback.run();
			} catch (Exception e) {
				log.error("Jobs rpc, server stopedCallback error.", e);
			}
		}
	}
}
