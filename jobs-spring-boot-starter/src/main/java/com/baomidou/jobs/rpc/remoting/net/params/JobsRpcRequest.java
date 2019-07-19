package com.baomidou.jobs.rpc.remoting.net.params;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * request
 *
 * @author xuxueli 2015-10-29 19:39:12
 */
@Data
@ToString
public class JobsRpcRequest implements Serializable{
	private String requestId;
	private long createMillisTime;
	private String accessToken;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
	private String version;

}
