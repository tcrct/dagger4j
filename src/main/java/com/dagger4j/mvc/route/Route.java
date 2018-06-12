package com.dagger4j.mvc.route;

import com.alibaba.fastjson.annotation.JSONField;

import java.lang.reflect.Method;

/**
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public class Route {

    private Mapping mapping;
    private Class<?> controllerClass;
    @JSONField(serialize=false, deserialize = false)
    private Method actionMethod;

    public Route(Mapping mapping, Class<?> controllerClass, Method actionMethod) {
        this.mapping = mapping;
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }
}
