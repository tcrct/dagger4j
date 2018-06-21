package com.dagger4j.mvc.scan;

import com.dagger4j.kit.PropKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.mvc.annotation.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 框架需要用到的类扫描策略
 * @author laotang on 2018/6/16.
 */
public class EntityClassStrategy extends AbstractScanClassStrategy {

    // entity class pool
    private static final Map<String, Class<?>> entityClassMap = new HashMap<>();
    private static EntityClassStrategy ourInstance = new EntityClassStrategy();

    private String packagePath;
    private List<String> jarNames;

    /**
     * 取配置文件里指定的包路径与jar文件前缀
     * @return
     */
    public static EntityClassStrategy getInstance() {
        return ourInstance;
    }

    private EntityClassStrategy(){
        this(PropKit.get(ConstEnums.BASE_PACKAGE_PAGE.getValue()) ,PropKit.getList(ConstEnums.JAR_PREFIX.getValue()));
    }

    public EntityClassStrategy(String packagePath, List<String> jarNames){
        this.packagePath = packagePath;
        this.jarNames = jarNames;
    }

    /**
     * 扫描Entity
     * @return
     */
    @Override
    public Map<String, Class<?>> getClassMap() {
        if(!entityClassMap.isEmpty()) {
            return entityClassMap;
        }
        List<Class<?>> classList = getAllClass(packagePath, jarNames);
        if(ToolsKit.isEmpty(classList)) {
            return null;
        }
        for(Class<?> clazz : classList) {
            Entity annotation = clazz.getAnnotation(Entity.class);
            if(ToolsKit.isNotEmpty(annotation)) {
                entityClassMap.put(clazz.getName(), clazz);
            }
        }
        return entityClassMap;
    }
}
