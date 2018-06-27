package com.dagger4j;

import com.dagger4j.doclet.DocletKit;

/**
 * Hello world!
 *
 */
public final class Dagger {

    public static void main( String[] args ) {

        DocletKit.duang().build();

//        Application.duang().port(12345)
//                .handles(new HandlerChain() {
//                    @Override
//                    public void before(List<IHandler> beforeList) {
//                        beforeList.add(new TestHttpHandler());
//                    }
//                    @Override
//                    public void after(List<IHandler> afterList) {
//                        afterList.add(new TestHttpHandler2());
//                    }
//                })
//                .plugins(new PluginChain() {
//                    @Override
//                    public void addPlugin(List<IPlugin> pluginList) {
//                        pluginList.add(new TestHttpPlugin());
//                    }
//                }).run();
    }
}
