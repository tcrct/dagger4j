package com.dagger4j.cache.client;

import com.dagger4j.cache.ds.RedisCacheSource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Created by laotang
 * @date createed in 2018/7/5.
 */
public class RedisClient extends AbstractCacheClient<Jedis> {

    private JedisPool jedisPool = null;

    public RedisClient() {
        jedisPool = new RedisCacheSource.Builder().build().getSource();
    }

    @Override
    public Jedis getClient() throws Exception {
        return jedisPool.getResource();
    }

    @Override
    public void close() throws Exception {
        jedisPool.close();
    }

/*
    public <T> T call(JedisAction cacheAction) {
        T result = null;
        if(!isCluster) {
            Jedis jedis = getJedis();
            try {
                result = (T) cacheAction.execute(jedis);
            } catch (Exception e) {
                JedisPoolUtils.returnBrokenResource(_clientExt.getJedisPool(), jedis);
                logger.warn(e.getMessage(), e);
            }
            finally {
                JedisPoolUtils.returnResource(_clientExt.getJedisPool(), jedis);
            }
        } else {
            JedisCluster jedisCluster = getJedisCluster();
            result = (T) cacheAction.execute(jedisCluster);
        }
        return result;
    }


    public String get(String key) throws Exception{
        return getClient().get(key);
    }
*/
    public void set(String key, Object value) {

    }
}
