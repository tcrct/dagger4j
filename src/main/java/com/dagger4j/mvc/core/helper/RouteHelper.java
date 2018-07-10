package com.dagger4j.mvc.core.helper;

import com.dagger4j.exception.MvcException;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.annotation.Controller;
import com.dagger4j.mvc.annotation.Mapping;
import com.dagger4j.mvc.core.BaseController;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.mvc.route.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 路由辅助类
 * @author Created by laotang
 * @date createed in 2018/6/22.
 */
public class RouteHelper {

    private static final Logger logger = LoggerFactory.getLogger(RouteHelper.class);
    // 普通风格
   private static Map<String, Route> routeMap = new HashMap<>();
    // restful风格
    private static Map<String, Route> restfulRouteMap = new HashMap<>();

    static {
        try {
            Set<String> excludedMethodName = ObjectKit.buildExcludedMethodName(BaseController.class);
            List<Class<?>> clontrllerClassList = ClassHelper.getClontrllerClassList();
            for (Class<?> controllerClass : clontrllerClassList) {
                if (!controllerClass.isAnnotationPresent(Controller.class)) {
                    logger.warn("Controller类["+controllerClass.getName()+ "]没有@Controller注解, 退出本次循环...");
                    continue;
                }
                Mapping controllerMapping = controllerClass.getAnnotation(Mapping.class);
                String controllerKey = buildMappingKey(controllerMapping, controllerClass.getSimpleName());
                // 遍历Controller类所有的方法
                Method[] actionMethods = controllerClass.getDeclaredMethods();
                for (Method actionMethod : actionMethods) {
                    //如果是Object, Controller公用方法名并且没有参数的方法, 则退出本次循环
                    if(ObjectKit.isExcludeMethod(actionMethod, excludedMethodName)) {
                        continue;
                    }
                    Route route = new Route(controllerClass, null, controllerKey, actionMethod);
                    String routeKey = route.getRequestMapping().getValue();
                    // 如果包含有{}的，则视为restful风格的URI
                    if (ToolsKit.isNotEmpty(routeKey) && routeKey.contains("{") && routeKey.contains("}")) {
                        restfulRouteMap.put(routeKey, route);
                    } else {
                        routeMap.put(routeKey, route);
                    }
                }
            }
            printRouteKey();
        } catch (Exception e) {
            logger.warn("RouteHelper 初始化失败：" + e.getMessage(), e);
            throw new MvcException(e.getMessage(), e);
        }
    }

    private static String buildMappingKey(Mapping mapping, String mappingKey) {
        if(ToolsKit.isNotEmpty(mapping) && ToolsKit.isNotEmpty(mapping.value())) {
            mappingKey = mapping.value();
        } else {
            if(mappingKey.toLowerCase().endsWith("controller")) {
                mappingKey = mappingKey.substring(0, mappingKey.length() - "controller".length());
            }
        }
        return mappingKey.endsWith("/") ? mappingKey.substring(0, mappingKey.length()-1).toLowerCase() : mappingKey.toLowerCase();
    }

    private static void printRouteKey() {
        List<String> keyList = new ArrayList<>(routeMap.keySet());
        keyList.addAll(restfulRouteMap.keySet());
        Collections.sort(keyList);
        logger.warn("**************** Controller Mapper Key ****************");
        for (String key : keyList) {
            if(key.startsWith(ConstEnums.FRAMEWORK_MAPPING_KEY.getValue())) {
                continue;
            }
            logger.warn(key);
        }
    }

    public static Map<String, Route> getRouteMap() {
        return routeMap;
    }

    public static Map<String, Route> getRestfulRouteMap() {
        return restfulRouteMap;
    }
}
