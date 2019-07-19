package com.baomidou.jobs.admin.service;

/**
 * Jobs 锁接口 , 实现该接口可以是数据库锁也可以是 redis 等
 *
 * @author jobob
 * @since 2019-07-13
 */
public interface IJobsLockService {

    /**
     * 插入一条记录，标志着占有锁
     *
     * @param name  锁的名称
     * @param owner 锁的持有者
     * @return 返回影响的记录行数
     */
    int insert(String name, String owner);

    /**
     * 释放锁
     *
     * @param name  锁的名称
     * @param owner 锁的持有者，不存在则根据 name 删除
     * @return 返回影响的记录行数
     */
    int delete(String name, String owner);
}
