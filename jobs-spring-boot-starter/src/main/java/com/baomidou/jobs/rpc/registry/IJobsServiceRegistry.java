package com.baomidou.jobs.rpc.registry;


import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Jobs 服务注册接口
 *
 * @author jobob
 * @since 2019-08-01
 */
public interface IJobsServiceRegistry {

    /**
     * 启动
     *
     * @param param 参数
     */
    void start(Map<String, String> param);

    /**
     * 停止
     */
    void stop();


    /**
     * registry service, for mult
     *
     * @param keys  service key
     * @param value service value/ip:port
     * @return
     */
    boolean registry(Set<String> keys, String value);


    /**
     * remove service, for mult
     *
     * @param keys
     * @param value
     * @return
     */
    boolean remove(Set<String> keys, String value);

    /**
     * discovery services, for mult
     *
     * @param keys
     * @return
     */
    Map<String, TreeSet<String>> discovery(Set<String> keys);

    /**
     * discovery service, for one
     *
     * @param key service key
     * @return service value/ip:port
     */
    TreeSet<String> discovery(String key);

}
