package com.dagger4j.mvc.scan;

import com.dagger4j.kit.PropKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.mvc.annotation.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 框架需要用到的类扫描策略
 * @author laotang on 2018/6/16.
 */
public class ControllerClassStrategy extends AbstractScanClassStrategy {

    // controller class pool
    private static final Map<String, Class<?>> clontrllerClassMap = new HashMap<>();
    private static ControllerClassStrategy ourInstance = new ControllerClassStrategy();

    private String packagePath;
    private List<String> jarNames;

    /**
     * 取配置文件里指定的包路径与jar文件前缀
     * @return
     */
    public static ControllerClassStrategy getInstance() {
        return ourInstance;
    }

    private ControllerClassStrategy(){
        this(PropKit.get(ConstEnums.BASE_PACKAGE_PAGE.getValue()) ,PropKit.getList(ConstEnums.JAR_PREFIX.getValue()));
    }

    public ControllerClassStrategy(String packagePath, List<String> jarNames){
        this.packagePath = packagePath;
        this.jarNames = jarNames;
    }

    /**
     * 默认为扫描Controller
     * @return
     */
    @Override
    public Map<String, Class<?>> getClassMap()  {
        if(!clontrllerClassMap.isEmpty()) {
            return clontrllerClassMap;
        }
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
