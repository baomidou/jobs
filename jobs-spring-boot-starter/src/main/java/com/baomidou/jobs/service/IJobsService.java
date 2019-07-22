package com.baomidou.jobs.service;

import com.baomidou.jobs.JobsConstant;
import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.jobs.model.param.RegistryParam;
import com.baomidou.jobs.trigger.JobsTrigger;
import com.baomidou.jobs.trigger.TriggerTypeEnum;

import java.util.List;
import java.util.UUID;

/**
 * Jobs Admin 接口
 *
 * @author jobob
 * @since 2019-07-18
 */
public interface IJobsService {

    /**
     * 节点注册
     *
     * @param registryParam 注册参数
     * @return
     */
    boolean registry(RegistryParam registryParam);

    /**
     * 手动触发任务ID执行
     *
     * @param jobId 任务ID
     * @param param 执行参数
     * @return
     */
    default boolean execute(Long jobId, String param) {
        return JobsTrigger.trigger(jobId, TriggerTypeEnum.MANUAL, -1, param);
    }

    /**
     * 待调度任务列表
     *
     * @param nextTime 下次执行时间
     * @return
     */
    List<JobsInfo> getJobsInfoList(long nextTime);

    /**
     * 根据 任务ID 获取任务信息对象
     *
     * @param id 任务 ID
     * @return
     */
    JobsInfo getJobsInfoById(Long id);

    /**
     * 根据 任务ID 更新任务信息
     *
     * @param jobsInfo 任务信息对象
     * @return
     */
    boolean updateJobsInfoById(JobsInfo jobsInfo);

    /**
     * 使用线程本地变量记录锁的持有者
     */
    ThreadLocal<String> ownerThreadLocal = new ThreadLocal<>();

    /**
     * 尝试获取锁
     *
     * @param lockKey 锁 KEY
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
        if (tryLock(lockKey, owner)) {
            ownerThreadLocal.set(owner);
            return true;
        }
        return false;
    }


    /**
     * 释放锁
     *
     * @param lockKey 锁 KEY
     * @param force   强制解锁
     */
    default void unlock(String lockKey, boolean force) {
        if (force) {
            unlock(lockKey, null);
        } else {
            String owner = ownerThreadLocal.get();
            if (null == owner) {
                throw new IllegalMonitorStateException("should not call unlock() without tryLock(()");
            }
            ownerThreadLocal.remove();
            if (!JobsConstant.OPERATION_TRY_LOCK.equals(owner)) {
                unlock(lockKey, owner);
            }
        }
    }

    /**
     * 插入一条记录，标志着占有锁
     *
     * @param name  锁的名称
     * @param owner 锁的持有者
     * @return 返回影响的记录行数
     */
    boolean tryLock(String name, String owner);

    /**
     * 释放锁
     *
     * @param name  锁的名称
     * @param owner 锁的持有者，不存在则根据 name 删除
     * @return 返回影响的记录行数
     */
    boolean unlock(String name, String owner);


    /**
     * 清理超时节点
     *
     * @return 影响行数
     */
    default int cleanTimeoutApp() {
        return removeTimeOutApp(JobsConstant.CLEAN_TIMEOUT);
    }

    /**
     * 移除超时节点
     *
     * @param timeout 超时时长
     * @return
     */
    int removeTimeOutApp(int timeout);

    /**
     * 移除节点
     *
     * @param registryParam 注册参数
     * @return
     */
    boolean removeApp(RegistryParam registryParam);


    /**
     * 查询注册地址列表
     *
     * @param app 客户端 APP 名称
     * @return
     */
    List<String> getAppAddressList(String app);


    /**
     * 保存或者根据 ID 更新日志
     *
     * @param jobsLog 日志对象
     * @return
     */
    boolean saveOrUpdateLogById(JobsLog jobsLog);
}
