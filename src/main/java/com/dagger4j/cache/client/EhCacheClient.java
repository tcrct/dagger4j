package com.dagger4j.cache.client;

import com.dagger4j.cache.ds.EhCacheSource;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;

/**
 * @author Created by laotang
 * @date createed in 2018/7/5.
 * https://blog.csdn.net/baidu_35776955/article/details/56676955
 */
public class EhCacheClient extends AbstractCacheClient<Cache> {

    private CacheManager cacheManager = null;
    private Cache ehCache;

    public EhCacheClient() {
        EhCacheSource ehCacheSource = new EhCacheSource.Builder().build();
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(ehCacheSource.getAlias(),
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,ehCacheSource.getSource()).build())
                .build(true);
        ehCache = cacheManager.getCache(ehCacheSource.getAlias(), String.class, Object.class);
    }

    @Override
    public Cache getClient() throws Exception {
        return ehCache;
    }

    @Override
    public void close() throws Exception {
        cacheManager.close();
    }

    public <T> T get(String key) throws Exception{
        return (T)ehCache.get(key);
    }

    public void set(String key, Object value) {
        ehCache.putIfAbsent(key, value);
    }

    public void delete(String key) {
        ehCache.remove(key);
    }
}
