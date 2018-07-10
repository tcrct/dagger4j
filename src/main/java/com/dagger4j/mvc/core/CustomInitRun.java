package com.dagger4j.mvc.core;

/**
 * @author Created by laotang
 * @date createed in 2018/7/10.
 */
public class CustomInitRun {

    private static CustomInitRun ourInstance = new CustomInitRun();

    public static CustomInitRun getInstance() {
        return ourInstance;
    }

    private InitRun customInitRun;

    private CustomInitRun() {

    }

    public void addRun(InitRun run) {
        this.customInitRun = run;
    }

    public void start() throws Exception {
        customInitRun.run();
    }


}
