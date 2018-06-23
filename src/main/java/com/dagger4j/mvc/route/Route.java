package com.dagger4j.mvc.route;

import com.dagger4j.kit.PathKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.annotation.Controller;
import com.dagger4j.mvc.http.enums.HttpMethod;
import com.dagger4j.mvc.annotation.Mapping;
import com.dagger4j.mvc.annotation.Validation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 路由
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public class Route {

    private RequestMapping requestMapping;//mapping注解对象类
    private Class<?> controllerClass;  //执行的控制器类
    private Method actionMethod;       // 执行的方法
    private HttpMethod[] httpMethod;  //请求类型
    private List<ValidationParam> validationParamList; //
    private boolean singleton; //是否单例

    public Route(Class<?> controllerClass, String controllerMappingKey, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
        builderMapping(controllerMappingKey, actionMethod);
    }
    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public RequestMapping getRequestMapping() {
        return requestMapping;
    }

    public void builderMapping(String controllerKey, Method actionMethod) {
        Mapping methodMapping = actionMethod.getAnnotation(Mapping.class);
        if(ToolsKit.isEmpty(methodMapping)) {
            return;
        }

        Validation[] paramArray = methodMapping.vtor();
        if(ToolsKit.isNotEmpty(paramArray)) {
            validationParamList = new ArrayList<>(paramArray.length);
            for (Validation validation : paramArray) {
                ValidationParam validationParam = null;
                Class<?> vtorClass = validation.bean();
                if (null != vtorClass && !Object.class.equals(vtorClass)) {
                    // TODO 如果有指定验证的entity或DTO类，则再对该类进行解释， 待完成
//                            validationParam =
                }
                validationParam = validationParamValue(validation);
                validationParamList.add(validationParam);
            }
        }

        this.httpMethod = methodMapping.method();
        String methodKey = PathKit.fixPath(methodMapping.value());
        String routeKey = methodKey;
        if(!controllerKey.equalsIgnoreCase(methodKey)) {
            routeKey = controllerKey + methodKey;
        }
        routeKey = PathKit.fixPath(routeKey);
        this.requestMapping = new RequestMapping(routeKey,
                methodMapping.desc(),
                methodMapping.order(),
                methodMapping.timeout(),
                validationParamList);

        // 是否单例
        setSingleton(controllerClass.getAnnotation(Controller.class).scope().equalsIgnoreCase("singleton"));
    }

    /**
     * 验证参数值
     * @param validation
     * @return
     */
    private ValidationParam validationParamValue(Validation validation) {
        ValidationParam validationParam = new ValidationParam(validation.isEmpty(), validation.length(), validation.range(),
                validation.fieldName(), validation.fieldValue(), validation.desc(), validation.formatDate(),
                validation.oid(), validation.fieldType(), validation.bean());

        //默认值的设置为null，不返回到客户端
        if(Object.class.equals(validationParam.getBeanClass())) {
            validationParam.setBeanClass(null);
        }
        if(!Date.class.equals(validationParam.getTypeClass())){
            validationParam.setFormatDate(null);
        }
        if(validationParam.getLength() ==0){
            validationParam.setLength(null);
        }

        return validationParam;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }
}

