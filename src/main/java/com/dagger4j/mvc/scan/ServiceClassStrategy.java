package com.dagger4j.mvc.scan;

import com.dagger4j.kit.PropKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.mvc.annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 框架需要用到的类扫描策略
 * @author laotang on 2018/6/16.
 */
public class ServiceClassStrategy extends AbstractScanClassStrategy {

    // service class pool
    private static final Map<String, Class<?>> serviceClassMap = new HashMap<>();
    private static ServiceClassStrategy ourInstance = new ServiceClassStrategy();

    private String packagePath;
    private List<String> jarNames;

    /**
     * 取配置文件里指定的包路径与jar文件前缀
     * @return
     */
    public static ServiceClassStrategy getInstance() {
        return ourInstance;
    }

    private ServiceClassStrategy(){
        this(PropKit.get(ConstEnums.BASE_PACKAGE_PAGE.getValue()) ,PropKit.getList(ConstEnums.JAR_PREFIX.getValue()));
    }

    public ServiceClassStrategy(String packagePath, List<String> jarNames){
        this.packagePath = packagePath;
        this.jarNames = jarNames;
    }

    /**
     * 默认为扫描Controller
     * @return
     */
    @Override
    public Map<String, Class<?>> getClassMap() {
        if(!serviceClassMap.isEmpty()) {
            return serviceClassMap;
        }
        List<Class<?>> classList = getAllClass(packagePath, jarNames);
        if(ToolsKit.isEmpty(classList)) {
            return null;
        }
        for(Class<?> clazz : classList) {
            Service annotation = clazz.getAnnotation(Service.class);
            if(ToolsKit.isNotEmpty(annotation)) {
                serviceClassMap.put(clazz.getName(), clazz);
            }
        }
        return serviceClassMap;
    }
}
