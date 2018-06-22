package com.dagger4j;

import com.dagger4j.mvc.http.handler.HandlerAdapter;
import com.dagger4j.mvc.http.handler.IHandler;
import com.dagger4j.mvc.plugin.IPlugin;
import com.dagger4j.server.Application;

import java.util.List;

/**
 * Hello world!
 *
 */
public final class Dagger {


    public static void main( String[] args )
    {
        Application.duang().port(12345).handles(new HandlerAdapter() {
                    @Override
                    public void before(List<IHandler> beforeHandlerList) {
                        beforeHandlerList.add(null);
                    }
                    @Override
                    public void after(List<IHandler> afterHandlerList) {
                    }
                }).plugins(new IPlugin() {
                    @Override
                    public void start() throws Exception {

                    }

                    @Override
                    public void stop() throws Exception {

                    }
                })
//                .handles(new IHttpHandler() {
//
//                    @Override
//                    public void addBeforeHandlers(List<IHandler> handlers) throws Exception {
//                        handlers.addHandler(new xxxxHandler())
//                        return null;
//                    }
//
//                    @Override
//                    public IHandler addAfterHandlers(IHandler handler) throws Exception {
//                        return null;
//                    }
//                })
//                .use()
                .run();
    }
}
