package com.dagger4j.mvc.core;

import com.dagger4j.exception.MvcException;
import com.dagger4j.kit.ClassKit;
import com.dagger4j.mvc.core.helper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Created by laotang
 * @date createed in 2018/6/19.
 */
public class StartContextListener {

    private static final Logger logger = LoggerFactory.getLogger(StartContextListener.class);

    private static StartContextListener ourInstance = new StartContextListener();

    public static StartContextListener getInstance() {
        return ourInstance;
    }

    /**
     *  netty启动后，需要对框架执行以下操作，顺序不可变更
     *  1，扫描类
     *  2，类实例化
     *  3，加载插件
     *  4，依赖注入
     *  5，注册路由
     *  6，执行自定义的初始化代码
     */
    private List<Class<?>> APP_CONTEXT_LISTENER = new ArrayList<Class<?>>(){
        {
            this.add(ClassHelper.class);
            this.add(BeanHelper.class);
            this.add(PluginHelper.class);
            this.add(IocHelper.class);
            this.add(RouteHelper.class);
        }
    };

    /**
     * 启动框架
     */
    public void start() {
        try {
            for(Iterator<Class<?>> it = APP_CONTEXT_LISTENER.iterator(); it.hasNext();) {
                ClassKit.loadClass(it.next());
            }
            initCode();
        } catch (Exception e) {
            throw new MvcException(e.getMessage(), e);
        }
    }

    /**
     * 执行自定义的初始化代码
     * @throws Exception
     */
    private void initCode() throws Exception {

    }

}
