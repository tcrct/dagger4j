package com.dagger4j.cache;

import com.dagger4j.cache.ds.ICacheSource;

/**
 * @author Created by laotang
 * @date createed in 2018/7/5.
 */
public class CacheSource<T> {

    private ICacheSource cacheQuery;

    public CacheSource(ICacheSource<T> cacheQuery) {
        this.cacheQuery = cacheQuery;
    }

    public T getCacheSource() {
        return (T)cacheQuery.getSource();
    }

}
