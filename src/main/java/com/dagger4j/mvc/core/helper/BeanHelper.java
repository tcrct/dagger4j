package com.dagger4j.mvc.core.helper;

import com.dagger4j.kit.ClassKit;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.kit.ThreadPoolKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * Bean辅助类
 * 用于将框架所需的类实例化成对象， 以供使用
 *
 * @author Created by laotang
 * @date createed in 2018/6/22.
 */
public class BeanHelper {

    private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    /**
     * Bean Map [已实例]
     */
    private static Map<String, List<Object>> beanMap = new ConcurrentHashMap<>();

    static {
        for(ConstEnums.ANNOTATION_CLASS annotationClass : ConstEnums.ANNOTATION_CLASS.values()) {
            // 如果需要实例化
            if(annotationClass.getInstance()) {
                String key = annotationClass.getName();
                List<Object> beanList = beanMap.get(key);
                if(ToolsKit.isEmpty(beanList)) {
                    beanList = new ArrayList<Object>();
                }
                List<Class<?>> classList = ClassHelper.getClassList(annotationClass.getClazz());
                if(ToolsKit.isNotEmpty(classList)) {
                    ThreadPoolKit.execute(new InstanceBeanTask(classList, beanList));
                }
                beanMap.put(key, beanList);
            }
        }
    }

    /**
     * 实例化对对象
     */
    static class InstanceBeanTask implements Runnable {

        private volatile List<Class<?>> classList;
        private volatile List<Object> beanList;

        public InstanceBeanTask(List<Class<?>> sourceClassList, List<Object> sourceBeanList) {
            classList = sourceClassList;
            beanList = sourceBeanList;
        }

        @Override
        public void run() {
            for(Iterator<Class<?>> iterator = classList.iterator(); iterator.hasNext();) {
                Class<?> clazz = iterator.next();
                // 是接口或抽象类则退出本次循环
                if(!ClassKit.supportInstance(clazz)) {
                    continue;
                }
                Object bean = ObjectKit.newInstance(clazz);
                if(ToolsKit.isNotEmpty(bean)) {
                    beanList.add(bean);
                }
            }
        }
    }
}
