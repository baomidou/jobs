package com.baomidou.jobs.rpc.remoting.net.params;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * response
 *
 * @author xuxueli 2015-10-29 19:39:54
 */
@Data
@ToString
public class JobsRpcResponse implements Serializable{
	private String requestId;
    private String errorMsg;
    private Object result;

}
