package com.dagger4j.mvc.scan;

import com.dagger4j.kit.PropKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.annotation.Entity;
import com.dagger4j.mvc.http.enums.ConstEnums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 框架需要用到的类扫描策略
 * @author laotang on 2018/6/16.
 */
public class ProxyClassStrategy extends AbstractScanClassStrategy {

    // entity class pool
    private static final Map<String, Class<?>> proxyClassMap = new HashMap<>();
    private static ProxyClassStrategy ourInstance = new ProxyClassStrategy();

    private String packagePath;
    private List<String> jarNames;

    /**
     * 取配置文件里指定的包路径与jar文件前缀
     * @return
     */
    public static ProxyClassStrategy getInstance() {
        return ourInstance;
    }

    private ProxyClassStrategy(){
        this(PropKit.get(ConstEnums.BASE_PACKAGE_PAGE.getValue()) ,PropKit.getList(ConstEnums.JAR_PREFIX.getValue()));
    }

    public ProxyClassStrategy(String packagePath, List<String> jarNames){
        this.packagePath = packagePath;
        this.jarNames = jarNames;
    }

    /**
     * 扫描Entity
     * @return
     */
    @Override
    public Map<String, Class<?>> getClassMap() {
        if(!proxyClassMap.isEmpty()) {
            return proxyClassMap;
        }
        List<Class<?>> classList = getAllClass(packagePath, jarNames);
        if(ToolsKit.isEmpty(classList)) {
            return null;
        }
        for(Class<?> clazz : classList) {
            Entity annotation = clazz.getAnnotation(Entity.class);
            if(ToolsKit.isNotEmpty(annotation)) {
                proxyClassMap.put(clazz.getName(), clazz);
            }
        }
        return proxyClassMap;
    }
}
