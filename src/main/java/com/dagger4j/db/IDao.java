package com.dagger4j.db;

/**
 * @author Created by laotang
 * @date createed in 2018/6/25.
 */
public interface IDao<Q, U> {

    /**
     * 保存对象
     * @param  entity		待保存的对象
     * @return		对象
     */
    <T> T save(T entity) throws Exception;

    /**
     * 根据条件更新字段
     * @param query			查询条件
     * @param update		更新内容
     * @return
     * @throws Exception
     */
    long update( Q query, U update) throws Exception;


    /**
     * 根据Query查找对象
     * @param key		属性字段
     * @param value		值
     * @return			对象
     */
    <T> T findOne(Q query) throws Exception;
}
