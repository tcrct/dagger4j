package com.dagger4j.cache.plugin;

import com.dagger4j.cache.client.eh.EhCacheClient;
import com.dagger4j.cache.ds.EhCacheAdapter;
import com.dagger4j.db.IClient;
import com.dagger4j.mvc.plugin.IPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * EhCache缓存插件
 * @author Created by laotang
 * @date createed in 2018/7/11.
 */
public class EhCachePlugin implements IPlugin {

    private final static Logger logger = LoggerFactory.getLogger(EhCachePlugin.class);
    private List<IClient> cacheClientList = new ArrayList<>();

    public EhCachePlugin(EhCacheAdapter adapter) {
        EhCacheClient redisClient = new EhCacheClient(adapter);
        this.cacheClientList.add(redisClient);
    }

    public EhCachePlugin(List<EhCacheAdapter> cacheSources) {
        for(EhCacheAdapter adapter : cacheSources) {
            EhCacheClient redisClient = new EhCacheClient(adapter);
            this.cacheClientList.add(redisClient);
        }
    }

    @Override
    public void start() throws Exception {
        for(IClient client : cacheClientList) {
            client.getClient();
            logger.warn("ehcache["+client.getId()+"] start success!");
        }
    }

    @Override
    public void stop() throws Exception {
        for(IClient client : cacheClientList) {
            client.close();
            logger.warn("ehcache["+client.getId()+"] close success!");
        }
    }
}
