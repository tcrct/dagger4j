package com.dagger4j.server;

import com.dagger4j.mvc.core.helper.HandlerHelper;
import com.dagger4j.mvc.core.helper.PluginHelper;
import com.dagger4j.mvc.http.handler.HandlerChain;
import com.dagger4j.mvc.plugin.PluginChain;
import com.dagger4j.server.common.BootStrap;
import com.dagger4j.server.netty.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by laotang on 2018/6/12.
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

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

    public Application handles(HandlerChain handlerChain) {
        HandlerHelper.setBefores(handlerChain.getBeforeHandlerList());
        HandlerHelper.setAfters(handlerChain.getAfterHandlerList());
        return application;
    }

    public Application plugins(PluginChain pluginChain) {
        try {
            PluginHelper.setPluginList(pluginChain.getPluginList());
        } catch (Exception e){
            logger.warn(e.getMessage(), e);
        }
        return application;
    }

    public Application init() {

        return application;
    }

    public void run() {
        BootStrap bootStrap = new BootStrap(host, port);
        new NettyServer(bootStrap).start();
    }
}
