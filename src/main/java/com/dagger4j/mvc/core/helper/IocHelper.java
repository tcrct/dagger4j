package com.dagger4j.mvc.core.helper;

import com.dagger4j.exception.MvcException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.annotation.Import;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * 依赖注入 辅助类
 * @author Created by laotang
 * @date createed in 2018/6/22.
 */
public class IocHelper {

    static {
        try {
            Map<String, Object> iocBeanMap = BeanHelper.getIocBeanMap();
            if(ToolsKit.isNotEmpty(iocBeanMap)) {
                for (Iterator<Map.Entry<String, Object>> iterator = iocBeanMap.entrySet().iterator(); iterator.hasNext(); ) {
                    Object bean = iterator.next().getValue();
                    if (null != bean) {
                        ioc(bean.getClass());
                    }
                }
            }
        } catch (Exception e) {
            throw new MvcException("依赖注入时异常: " + e.getMessage(), e);
        }
    }

    /**
     *  对Class下带有指定注解的Field进行注入对象
     * @param beanClass     需要对属性值注入对象的Class
     * @throws Exception
     */
    public static void ioc(Class<?> beanClass) throws Exception {
        Field[] fields = beanClass.getDeclaredFields();
        for(Field field : fields) {
            if (field.isAnnotationPresent(Import.class)) {
                Class<?> fieldTypeClass = field.getType();
                if (fieldTypeClass.equals(beanClass)) {
                    throw new MvcException(beanClass.getSimpleName() + " can't not already import " + fieldTypeClass.getSimpleName());
                }
                Object iocObj = BeanHelper.getBean(fieldTypeClass, beanClass);
                if(ToolsKit.isNotEmpty(iocObj)) {
                    field.setAccessible(true);
                    field.set(BeanHelper.getBean(beanClass), iocObj);
                }
            }
        }
    }
}
