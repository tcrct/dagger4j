package com.dagger4j.mvc.core;

import com.dagger4j.exception.MvcException;
import com.dagger4j.mvc.scan.ScanClassFactory;

/**
 * @author Created by laotang
 * @date createed in 2018/6/19.
 */
public class StartContextListener {

    private static StartContextListener ourInstance = new StartContextListener();
    /**
     * 取配置文件里指定的包路径与jar文件前缀
     * @return
     */
    public static StartContextListener getInstance() {
        return ourInstance;
    }

    /**
     *  netty启动后，需要对框架执行以下操作
     *  1，扫描类
     *  2，加载插件
     *  3，依赖注入
     *  4，注册路由
     *  5，执行自定义的初始化代码
     *
     *  以上步骤不可变更
     */
    public void start() {
        try {
            scanClass();
            loadPlugin();
            ioc();
            regRoute();
            initCode();
        } catch (Exception e) {
            throw new MvcException(e.getMessage(), e);
        }
    }


    /**
     * 扫描类
     * @throws Exception
     */
    private void scanClass() throws Exception {
        ScanClassFactory.getControllerClassMap()
    }

    private void loadPlugin() throws Exception {

    }

    private void ioc() throws Exception {

    }

    private void regRoute() throws Exception {

    }

    private void initCode() throws Exception {

    }

}
