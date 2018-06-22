package com.dagger4j.mvc.core.helper;

import com.dagger4j.exception.MvcException;
import com.dagger4j.kit.ToolsKit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean辅助类
 * 用于将框架所需的类实例化成对象， 以供使用
 *
 * @author Created by laotang
 * @date createed in 2018/6/22.
 */
public class BeanHelper {

    /**
     * Bean Map [已实例]
     */
    private static Map<String, Object> beanMap = new ConcurrentHashMap<String, Object>();
    private static Map<String, Object> controllerBeanMap = new ConcurrentHashMap<String, Object>();
    private static Map<String, Object> serviceBeanMap = new ConcurrentHashMap<String, Object>();


    static {
        Map<String, List<Class<?>>> allClassMap = ClassHelper.getClassMap();
        if(ToolsKit.isEmpty(allClassMap)) {
            throw new MvcException("class is null");
        }

    }
}
