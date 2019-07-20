package com.baomidou.jobs.model.param;

/**
 * 任务注册节点状态枚举
 *
 * @author jobob
 * @since 2019-07-20
 */
public enum RegisterStatusEnum {
    /**
     * 启用
     */
    ENABLED(0),
    /**
     * 已禁用
     */
    DISABLED(1);

    private Integer value;

    RegisterStatusEnum(Integer value){
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
