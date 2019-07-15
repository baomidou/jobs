package com.baomidou.jobs.starter.lock;

import com.baomidou.jobs.starter.JobsConstant;

import java.util.UUID;

/**
 * Jobs 锁接口
 *
 * @author jobob
 * @since 2019-07-13
 */
public interface IJobsLock {
    /**
     * 使用线程本地变量记录锁的持有者
     */
    ThreadLocal<String> ownerThreadLocal = new ThreadLocal<>();

    /**
     * 尝试获取锁
     *
     * @return 返回true代表已经获得锁，false代表获取锁失败（锁已经被别的进程占有）
     */
    default boolean tryLock(String lockKey) {
        String owner = ownerThreadLocal.get();
        if (null != owner && !owner.equals(JobsConstant.OPERATION_TRY_LOCK)) {
            // already hold a lock
            return true;
        }
        ownerThreadLocal.set(JobsConstant.OPERATION_TRY_LOCK);
        owner = UUID.randomUUID().toString();
        int affectRows = insert(lockKey, owner);
        if (affectRows > 0) {
            ownerThreadLocal.set(owner);
            return true;
        }
        return false;
    }


    /**
     * 释放锁
     * 如果之前从未调用过任何尝试获取锁的方法，那么抛出异常 IllegalMonitorStateException
     * 如果之前尝试获取锁，但是没有成功，调用unlock()不会产生任何副作用
     */
    default void unlock(String lockKey) {
        String owner = ownerThreadLocal.get();
        if (owner == null) {
            throw new IllegalMonitorStateException("should not call unlock() without tryLock()/lock()/lockInterruptibly()");
        }
        ownerThreadLocal.remove();
        if (!JobsConstant.OPERATION_TRY_LOCK.equals(owner)) {
            delete(lockKey, owner);
        }
    }

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
     * @param owner 锁的持有者
     * @return 返回影响的记录行数
     */
    int delete(String name, String owner);
}
