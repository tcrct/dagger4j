package com.dagger4j.mvc.plugin;


/**
 * 插件类接口
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public class TestHttpPlugin implements IPlugin {

    @Override
    public void start() throws Exception {
        System.out.println("start");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stop");
    }
}
