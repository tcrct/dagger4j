package com.dagger4j.mvc.core;

import com.dagger4j.annotation.Bean;
import com.dagger4j.db.IdEntity;
import com.dagger4j.exception.MvcException;
import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.utils.DataType;
import com.dagger4j.vtor.VtorKit;
import com.dagger4j.vtor.core.VtorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * 参数注入到方法体, 先根据参数名取出请求对象里的值后，再判断是否存在验证注解，如果有验证注解，则验证
 * 验证不通过，则抛出异常中止流程
 *
 * @author Created by laotang
 * @date createed in 2018/6/28.
 */
public class ParameterInvokeMethod {

    /**
     * 将请求参数转换为Object[], 注入到Method里
     *
     * @param controller       执行的控制器
     * @param method         执行的方法
     * @param paramNameArray 执行方法里的参数变量名
     * @return
     */
    public static Object[] getParameterValues(BaseController controller, Method method, String[] paramNameArray) {
        Parameter[] actionParams = method.getParameters();
        if (ToolsKit.isEmpty(actionParams)) {
            return  null;
        }
        Object[] requestParamValueObj = null;
        if (actionParams.length != paramNameArray.length) {
            throw new MvcException("参数长度不一致!");
        }
        IRequest request = controller.getRequest();
        requestParamValueObj = new Object[actionParams.length];
        for (int i = 0; i < actionParams.length; i++) {
            Class<?> parameterType = actionParams[i].getType();
            Annotation[] annotations = actionParams[i].getAnnotations();
            String paramValue = request.getParameter(paramNameArray[i]);
            if (DataType.isString(parameterType)) {
                requestParamValueObj[i] = paramValue;
            } else if (DataType.isInteger(parameterType) || DataType.isIntegerObject(parameterType)) {
                requestParamValueObj[i] = Integer.parseInt(paramValue);
            } else if (DataType.isLong(parameterType) || DataType.isLongObject(parameterType)) {
                requestParamValueObj[i] = Long.parseLong(paramValue);
            } else if (DataType.isDouble(parameterType) || DataType.isDoubleObject(parameterType)) {
                requestParamValueObj[i] = Double.parseDouble(paramValue);
            } else if (DataType.isDate(parameterType)) {
                requestParamValueObj[i] = ToolsKit.parseDate(paramValue, ConstEnums.DEFAULT_DATE_FORMAT_VALUE.getValue());
            } else if (DataType.isTimestamp(parameterType)) {
                requestParamValueObj[i] = ToolsKit.parseDate(paramValue, ConstEnums.DEFAULT_DATE_FORMAT_VALUE.getValue());
            } else if(DataType.isListType(parameterType) || DataType.isSetType(parameterType) || DataType.isQueueType(parameterType)) {
                requestParamValueObj[i] = invokeCollention(request, parameterType);
            } else if(DataType.isMapType(parameterType) ) {
                requestParamValueObj[i] = invokeMap(request);
            } else{
                requestParamValueObj[i] = invokeBean(request, parameterType, annotations, i);
            }

            //返回前，根据验证注解，进行参数数据验证
            if (ToolsKit.isNotEmpty(annotations)) {
                try {
                    // 有可能会有多个注解，所以要遍历一下
                    for (Annotation annotation : annotations) {
//                        System.out.println(annotation.annotationType() + "                      " + parameterType.getName() + "                  " + paramNameArray[i] + "              " + paramValue);
                        VtorFactory.validator(annotation, parameterType, paramNameArray[i], paramValue);
                    }
                } catch (Exception e) {
                    throw new ValidatorException(e.getMessage(), e);
                }
            }
        }
        return requestParamValueObj;
    }

    private static Object invokeCollention(IRequest request, Class<?> parameterType) {
        String json = request.getParameter(ConstEnums.INPUTSTREAM_STR_NAME.getValue());
        List entityList = ToolsKit.jsonParseArray(json, parameterType);
        try {
            VtorKit.validate(entityList);
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage(), e);
        }
        return entityList;
    }

    private static Object invokeMap(IRequest request) {
        String json = request.getParameter(ConstEnums.INPUTSTREAM_STR_NAME.getValue());
        Map entityMap = ToolsKit.jsonParseObject(json, Map.class);
        try {
            VtorKit.validate(entityMap);
        }  catch (Exception e) {
            throw new ValidatorException(e.getMessage(), e);
        }
        return entityMap;
    }

    private static Object invokeBean(IRequest request, Class<?> parameterType, Annotation[] annotation, int index) {
        // 如果是继承了IdEntity或对象有设置VtorBean注解或在参数前设置了Bean注解， 则认为是要转换为Bean对象并验证
        boolean isBean = parameterType.isAnnotationPresent(Bean.class)
                || DataType.isIdEntityType(parameterType)
                || (ToolsKit.isNotEmpty(annotation) && Bean.class.equals(annotation[index].annotationType()));
        if(!isBean) {
            return null;
        }
        String json = request.getParameter(ConstEnums.INPUTSTREAM_STR_NAME.getValue());
        Object entity = ToolsKit.jsonParseObject(json, parameterType);
        // 如果Bean的话，无需在参数添加注解，遍历bean里的field进行判断是否需要验证
        try {
            if(isBean&& ToolsKit.isNotEmpty(entity)) {VtorKit.validate(entity);}
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage(), e);
        }
        return entity;
    }

}
