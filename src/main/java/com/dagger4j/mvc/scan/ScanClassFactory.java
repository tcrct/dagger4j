package com.dagger4j.mvc.scan;

import com.dagger4j.kit.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 类扫描工厂
 * Created by laotang on 2018/6/15.
 */
public class ScanClassFactory {

    private static final Logger logger = LoggerFactory.getLogger(ScanClassFactory.class);
    private static ControllerClassStrategy controllerClassStrategy;
    private static ServiceClassStrategy serviceClassStrategy;
    private static EntityClassStrategy entityClassStrategy;
    private static ProxyClassStrategy proxyClassStrategy;


    /**
     *  扫描所有Controller, Service, Entity类
     */
    public static void initScanClass() throws Exception {
        getControllerClassMap();
        getServiceClassMap();
        getEntityClassMap();
        getProxyClassMap();
    }

    /**
     * 取出所有Controller类
     * @return
     */
    public static Map<String,Class<?>> getControllerClassMap() {
        if(ToolsKit.isEmpty(controllerClassStrategy)) {
            controllerClassStrategy = ControllerClassStrategy.getInstance();
        }
        return controllerClassStrategy.getClassMap();
    }

    /**
     * 取出所有Service类
     * @return
     */
    public static Map<String,Class<?>> getServiceClassMap() {
        if(ToolsKit.isEmpty(serviceClassStrategy)) {
            serviceClassStrategy = ServiceClassStrategy.getInstance();
        }
        return serviceClassStrategy.getClassMap();
    }

    /**
     * 取出所有Entity类
     * @return
     */
    public static Map<String,Class<?>> getEntityClassMap() {
        if(ToolsKit.isEmpty(entityClassStrategy)) {
            entityClassStrategy = EntityClassStrategy.getInstance();
        }
        return entityClassStrategy.getClassMap();
    }

    /**
     * 取出所有Proxy类
     * @return
     */
    public static Map<String,Class<?>> getProxyClassMap() {
        if(ToolsKit.isEmpty(proxyClassStrategy)) {
            proxyClassStrategy = ProxyClassStrategy.getInstance();
        }
        return proxyClassStrategy.getClassMap();
    }

}
