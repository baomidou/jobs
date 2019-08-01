package com.baomidou.jobs.rpc.registry.client.model;

import lombok.ToString;

import java.util.Objects;

/**
 * @author xuxueli 2018-12-03
 */
@ToString
public class JobsRegistryDataParamVO {
    private String key;
    private String value;

    public JobsRegistryDataParamVO() {

    }

    public JobsRegistryDataParamVO(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobsRegistryDataParamVO that = (JobsRegistryDataParamVO) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
