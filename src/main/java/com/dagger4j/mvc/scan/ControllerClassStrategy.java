package com.dagger4j.mvc.scan;

import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.ioc.Controller;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by laotang on 2018/6/16.
 */
public class ControllerClassStrategy extends AbstractScanClassStrategy {

    // controller class pool
    private static final Map<String, Class<?>> clontrllerClassMap = new HashMap<>();

    private String packagePath;
    private List<String> jarNames;

    public ControllerClassStrategy(String packagePath, List<String> jarNames){
        this.packagePath = packagePath;
        this.jarNames = jarNames;
    }

    @Override
    public Map<String, Class<?>> getClassMap() {
        List<Class<?>> classList = getAllClass(packagePath, jarNames);
        if(ToolsKit.isEmpty(classList)) {
            return null;
        }
        for(Class<?> clazz : classList) {
            Controller annotation = clazz.getAnnotation(Controller.class);
            if(ToolsKit.isNotEmpty(annotation)) {
                clontrllerClassMap.put(clazz.getName(), clazz);
            }
        }
        return clontrllerClassMap;
    }
}
