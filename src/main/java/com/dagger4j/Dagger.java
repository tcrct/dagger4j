package com.dagger4j;

import com.dagger4j.mvc.http.handler.HandlerChain;
import com.dagger4j.mvc.http.handler.TestHttpHandler;
import com.dagger4j.mvc.http.handler.TestHttpHandler2;
import com.dagger4j.mvc.http.handler.IHandler;
import com.dagger4j.mvc.plugin.TestHttpPlugin;
import com.dagger4j.mvc.plugin.IPlugin;
import com.dagger4j.mvc.plugin.PluginChain;
import com.dagger4j.server.Application;

import java.util.List;

/**
 * Hello world!
 *
 */
public final class Dagger {

    public static void main( String[] args ) {

        Application.duang().port(12345)
                .handles(new HandlerChain() {
                    @Override
                    public void before(List<IHandler> beforeList) {
                        beforeList.add(new TestHttpHandler());
                    }
                    @Override
                    public void after(List<IHandler> afterList) {
                        afterList.add(new TestHttpHandler2());
                    }
                })
                .plugins(new PluginChain() {
                    @Override
                    public void addPlugin(List<IPlugin> pluginList) {
                        pluginList.add(new TestHttpPlugin());
                    }
                }).run();
    }
}
