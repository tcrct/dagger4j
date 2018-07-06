package com.dagger4j.cache.ds;

import org.ehcache.config.builders.ResourcePoolsBuilder;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @author Created by laotang
 * @date createed in 2018/7/5.
 */
public class EhCacheSource extends AbstractCacheSource<ResourcePoolsBuilder> {

    private String alias = "dagger_ehcache";       //别名
    private int heap;            // 堆内存大少？？？

    public EhCacheSource(String alias, int heap) {
        this.alias = alias;
        this.heap = heap;
    }


    public String getAlias() {
        return alias;
    }

    public int getHeap() {
        return heap;
    }

    public static class Builder {
        private String alias;
        private int heap;

        public Builder() {

        }

        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }
        public Builder heap(int heap) {
            this.heap = heap;
            return this;
        }

        public EhCacheSource build() {
            return new EhCacheSource(alias, heap);
        }
    }

    @Override
    protected ResourcePoolsBuilder builderDataSource() {
        try {
        return ResourcePoolsBuilder.heap(heap);
        }catch(Exception e){
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        }
    }

}
