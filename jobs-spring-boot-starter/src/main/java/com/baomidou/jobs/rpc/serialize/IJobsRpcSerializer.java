package com.baomidou.jobs.rpc.serialize;

/**
 * Rpc 序列化接口
 *
 * @author jobob
 * @since 2019-11-01
 */
public interface IJobsRpcSerializer {

    /**
     * 序列化对象
     *
     * @param obj 对象
     * @param <T> 返回序列化字节数组
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化字节数组为类对象
     *
     * @param bytes 字节数组
     * @param clazz 待反序列化类
     * @param <T>   反序列化对象
     */
    <T> Object deserialize(byte[] bytes, Class<T> clazz);
}
