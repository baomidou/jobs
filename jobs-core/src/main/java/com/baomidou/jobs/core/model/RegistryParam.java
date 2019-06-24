package com.baomidou.jobs.core.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 注册参数
 *
 * @author xxl jobob
 * @since 2019-06-15
 */
@Data
@ToString
public class RegistryParam implements Serializable {
    private String registryGroup;
    private String registryKey;
    private String registryValue;

    public RegistryParam() {
        // to do nothing
    }

    public RegistryParam(String registryGroup, String registryKey, String registryValue) {
        this.registryGroup = registryGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
    }
}
