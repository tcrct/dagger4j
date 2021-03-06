package com.dagger4j.cache;


/**
 * @author Created by laotang
 * @date createed in 2018/5/17.
 */
public class CacheModelOptions {

    private String keyPrefix;
    private String customKey;
    private Integer ttl;
    private String keyDesc;

    public static class Builder {

        private String customKey;
        private String keyPrefix;
        private int ttl;
        private String keyDesc;

        public Builder(ICacheKeyEnums enums) {
            this.keyPrefix = enums.getKeyPrefix();
            this.ttl = enums.getKeyTTL();
            this.keyDesc = enums.getKeyDesc();
        }

        public Builder customKey(String customKey) {
            this.customKey = customKey;
            return this;
        }

        public CacheModelOptions builder() {
            return new CacheModelOptions(this);
        }
    }


    private CacheModelOptions(Builder builder) {
        keyPrefix = builder.keyPrefix;
        customKey = builder.customKey;
        ttl = builder.ttl;
        keyDesc = builder.keyDesc;
    }


    public String getKey() {
        return keyPrefix.endsWith(":") ? keyPrefix + customKey : keyPrefix+":"+customKey;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public Integer getKeyTTL() {
        return ttl;
    }

    public String getKeyDesc() {
        return keyDesc;
    }
}
