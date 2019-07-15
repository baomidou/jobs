package com.baomidou.jobs.starter.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 注册参数
 *
 * @author jobob
 * @since 2019-07-13
 */
@Data
@ToString
public class RegistryParam implements Serializable {
    private String app;
    private String address;

    public RegistryParam() {
        // to do nothing
    }

    public RegistryParam(String app, String address) {
        this.app = app;
        this.address = address;
    }
}
