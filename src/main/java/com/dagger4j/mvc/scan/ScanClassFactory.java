package com.dagger4j.mvc.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类扫描工厂
 * Created by laotang on 2018/6/15.
 */
public class ScanClassFactory {

    private static final Logger logger = LoggerFactory.getLogger(ScanClassFactory.class);


    public static List<Class<?>> getAllClass(String packagePath, List<String> jarNames) {
        return new ClassTemplate(packagePath, jarNames){
            @Override
            public void checkAndAddClass(Class<?> clazz, List<Class<?>> classList) {
                classList.add(clazz);
            }
        }.getList();
    }

}
