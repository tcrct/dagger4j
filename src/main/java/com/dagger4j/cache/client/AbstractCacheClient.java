package com.dagger4j.cache.client;

import com.dagger4j.db.DBConnect;
import com.dagger4j.db.IClient;
import com.dagger4j.utils.DaggerId;

/**
 * @author Created by laotang
 * @date createed in 2018/7/6.
 */
public abstract class AbstractCacheClient<T> implements IClient<T> {

    public abstract String getId();

    @Override
    public DBConnect getDbConnect() {
        return null;
    }

    @Override
    public abstract T getClient() throws Exception;

    @Override
    public abstract void close() throws Exception;
}
