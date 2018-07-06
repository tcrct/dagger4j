package com.dagger4j.cache.client;

/**
 * 缓存的执行方法接口
 * @author laotang
 */
public interface JedisAction<T> {
	
	T execute(Jedis jedisObj);
}
