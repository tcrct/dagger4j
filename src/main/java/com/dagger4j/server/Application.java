package com.dagger4j.server;

import com.dagger4j.mvc.http.handler.IHttpHandler;
import com.dagger4j.mvc.plugin.IPlugin;
import com.dagger4j.server.common.BootStrap;
import com.dagger4j.server.netty.NettyServer;

/**
 * Created by laotang on 2018/6/12.
 */
public class Application {

    private String host;
    private int port;
    private static Application application;

    public static Application duang() {
        if(application == null) {
            application = new Application();
        }
        return application;
    }

    public Application dev() {
        return application;
    }

    public Application host(String host) {
        this.host = host;
        return application;
    }

    public Application port(int port) {
        this.port = port;
        return application;
    }

    public Application handles(IHttpHandler handlerChain) {
        return application;
    }

    public Application plugins(IPlugin pluginChain) {
        return application;
    }

    public Application plugins() {
        return application;
    }

    public void run() {
        BootStrap bootStrap = new BootStrap(host, port);
        new NettyServer(bootStrap).start();
    }
}
