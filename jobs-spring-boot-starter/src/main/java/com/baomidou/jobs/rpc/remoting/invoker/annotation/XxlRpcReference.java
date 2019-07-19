package com.baomidou.jobs.rpc.remoting.invoker.annotation;

import com.baomidou.jobs.rpc.remoting.invoker.call.CallType;
import com.baomidou.jobs.rpc.remoting.invoker.route.LoadBalance;
import com.baomidou.jobs.rpc.remoting.net.NetEnum;
import com.baomidou.jobs.rpc.serialize.Serializer;

import java.lang.annotation.*;

/**
 * rpc service annotation, skeleton of stub ("@Inherited" allow service use "Transactional")
 *
 * @author 2015-10-29 19:44:33
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XxlRpcReference {

    NetEnum netType() default NetEnum.NETTY;

    Serializer.SerializeEnum serializer() default Serializer.SerializeEnum.HESSIAN;

    CallType callType() default CallType.SYNC;

    LoadBalance loadBalance() default LoadBalance.ROUND;

    //Class<?> iface;
    String version() default "";

    long timeout() default 1000;

    String address() default "";

    String accessToken() default "";

    //XxlRpcInvokeCallback invokeCallback() ;

}
