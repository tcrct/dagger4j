package com.dagger4j.cache.ds;

import com.dagger4j.kit.PropKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @author Created by laotang
 * @date createed in 2018/7/5.
 */
public class RedisCacheSource extends AbstractCacheSource<JedisPool> {

    private int database;
    private String host;
    private String password;
    private int port;
    private int timeout;

    private RedisCacheSource(int database, String host, String password, int port, int timeout) {
        this.database = database;
        this.host = host;
        this.password = password;
        this.port = port;
        this.timeout = timeout;
    }

    public static class Builder {
        private int database;
        private String host;
        private String password;
        private int port;
        private int timeout;

        public Builder() {

        }

        public Builder database(int database) {
            this.database = database;
            return this;
        }
        public Builder host(String host) {
            this.host = host;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder port(int port) {
            this.port = port;
            return this;
        }
        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public RedisCacheSource build() {
            return new RedisCacheSource(database, host, password, port, timeout);
        }
    }



    @Override
    protected JedisPool builderDataSource() {
        JedisPool pool = null;
        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxIdle(100);
        config.setMinIdle(10);
        config.setMaxTotal(100);
        config.setMaxWaitMillis(5000);
        config.setTestWhileIdle(false);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(false);
        config.setNumTestsPerEvictionRun(10);
        config.setMinEvictableIdleTimeMillis(1000);
        config.setSoftMinEvictableIdleTimeMillis(10);
        config.setTimeBetweenEvictionRunsMillis(10);
        config.setLifo(false);
        // 创建连接池
        try{
            database = ToolsKit.isEmpty(database) ? PropKit.getInt(ConstEnums.PROPERTIES.REDIS_DATABASE.getValue(),0) : database;
            host = ToolsKit.isEmpty(database) ? PropKit.get(ConstEnums.PROPERTIES.REDIS_HOST.getValue(),"127.0.0.1") : host;
            password =ToolsKit.isEmpty(database) ? PropKit.get(ConstEnums.PROPERTIES.REDIS_PWD.getValue(),"") : password;
            port = ToolsKit.isEmpty(database) ? PropKit.getInt(ConstEnums.PROPERTIES.REDIS_PORT.getValue(),6371) : port;
            timeout = ToolsKit.isEmpty(database) ? PropKit.getInt(ConstEnums.PROPERTIES.REDIS_PORT.getValue(),2000) : timeout;
            if(ToolsKit.isEmpty(password)) {
                if(host.contains(":")) {
                    String[] hostArray = host.split(":");
                    if(ToolsKit.isNotEmpty(hostArray)) {
                        try{
                            host = hostArray[0];
                            port = Integer.parseInt(hostArray[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                pool = new JedisPool(config, host, port, timeout);
            } else {
                pool = new JedisPool(config, host, port, timeout, password, database);
            }
            System.out.println("Connent  " + host + ":"+port +" Redis is Success...");
            return pool;
        }catch(Exception e){
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        }
    }

}
