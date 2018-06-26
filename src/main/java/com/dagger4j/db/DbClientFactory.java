package com.dagger4j.db;

import com.dagger4j.kit.ToolsKit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Created by laotang
 * @date createed in 2018/6/26.
 */
public class DbClientFactory {

    /**
     * 所有实例客户端的Pool，注意KEY的命令，要确保唯一性
     */
    private static ConcurrentMap<String, IClient> DB_CLIENT_MAP = new ConcurrentHashMap<>();

    /**
     * 添加数据库客户端到缓存池
     * @param dbClient
     */
    public static void setDbClient(IClient dbClient) {
        if(ToolsKit.isEmpty(dbClient)) {
            throw new NullPointerException("db client is null");
        }
        String dbClientId = dbClient.getId();
        if(!DB_CLIENT_MAP.containsKey(dbClientId)) {
            DB_CLIENT_MAP.put("", dbClient);
        }
    }

    /**
     * 根据名称返回客户端实例
     * @param name      保存在缓存池里的客户端的名称
     * @param <T>       泛型
     * @return
     */
    public static <T> T getDbClient(String dbClientId) {
        return (T)DB_CLIENT_MAP.get(dbClientId);
    }


}
