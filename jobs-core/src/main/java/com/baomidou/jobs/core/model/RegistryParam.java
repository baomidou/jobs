package com.baomidou.jobs.core.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by xuxueli on 2017-05-10 20:22:42
 */
@Data
@ToString
public class RegistryParam implements Serializable {
    private String registGroup;
    private String registryKey;
    private String registryValue;

    public RegistryParam() {
    }

    public RegistryParam(String registGroup, String registryKey, String registryValue) {
        this.registGroup = registGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
    }
}
