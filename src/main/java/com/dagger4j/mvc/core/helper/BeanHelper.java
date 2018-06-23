package com.dagger4j.mvc.core.helper;

import com.dagger4j.exception.MvcException;
import com.dagger4j.kit.ClassKit;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.kit.ThreadPoolKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;

import java.util.*;
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
    /**
     * ioc所需要的bean
     */
    private static Map<Class<?>, Object> iocBeanMap = new HashMap<>();

    /**
     * 实例化bean用的临时任务集合
     */
    private static Map<String, FutureTask<List<Object>>> futureTaskMap = new HashMap<>();

    static {
        for (ConstEnums.ANNOTATION_CLASS annotationClass : ConstEnums.ANNOTATION_CLASS.values()) {
            // 如果需要实例化
            if (annotationClass.getInstance()) {
                String key = annotationClass.getClazz().getName();
                List<Object> beanList = beanMap.get(key);
                if (ToolsKit.isEmpty(beanList)) {
                    beanList = new ArrayList<>();
                }
                List<Class<?>> classList = ClassHelper.getClassList(annotationClass.getClazz());
                if (ToolsKit.isNotEmpty(classList)) {
                    FutureTask<List<Object>> futureTask = ThreadPoolKit.execute(new InstanceBeanTask(classList, beanList));
                    futureTaskMap.put(key, futureTask);
                }
            }
        }
        try {
            for( Iterator<Map.Entry<String, FutureTask<List<Object>>>> iterator = futureTaskMap.entrySet().iterator(); iterator.hasNext();){
                Map.Entry<String, FutureTask<List<Object>>> entry = iterator.next();
                beanMap.put(entry.getKey(), entry.getValue().get());
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 返回所有Bean，其中：</br>
     *          key为<p>ConstEnums.ANNOTATION_CLASS</p>里指定的Name,
     *          value为Bean的List集合
     * @return  Map<String, List<Object>>
     */
    public static Map<String, List<Object>> getBeanMap() {
        return beanMap;
    }

    public static List<Object> getControllerBeanList() {
        return beanMap.get(ConstEnums.ANNOTATION_CLASS.CONTROLLER_ANNOTATION.getName());
    }
    public static List<Object> getServiceBeanList() {
        return beanMap.get(ConstEnums.ANNOTATION_CLASS.SERVICE_ANNOTATION.getName());
    }
    public static List<Object> getEntityBeanList() {
        return beanMap.get(ConstEnums.ANNOTATION_CLASS.ENTITY_ANNOTATION.getName());
    }

    /**
     *  根据Class取出对应的Ioc Bean
     */
    public static <T> T getBean(Class<?> clazz, Object targetObj) {
        if (!iocBeanMap.containsKey(clazz) && !targetObj.getClass().equals(Class.class)) {
            throw new MvcException(targetObj.getClass().getName() + " 无法根据类名获取实例: " + clazz + " , 请检查是否后缀名是否正确！");
        }
        return (T)iocBeanMap.get(clazz);
    }

    /**
     *  根据Class取出对应的Ioc Bean
     */
    public static <T> T getBean(Class<?> clazz) {
        if (!iocBeanMap.containsKey(clazz)) {
            throw new MvcException("无法根据类名["+clazz.getName()+"]获取实例 , 请检查！");
        }
        return (T)iocBeanMap.get(clazz);
    }

    /**
     * 所需要的Ioc Bean集合
     * @return
     */
    public static Map<Class<?>, Object> getIocBeanMap() {
        if(ToolsKit.isEmpty(iocBeanMap)) {

            List<Object> iocBeanList = new ArrayList<Object>() {
                {
                    this.addAll(getControllerBeanList());
                    this.addAll(getServiceBeanList());
                }
            };

            for (Object bean : iocBeanList) {
                iocBeanMap.put(bean.getClass(), bean);
            }
        }
        return iocBeanMap;
    }

    /**
     * 实例化对对象
     */
    static class InstanceBeanTask implements Callable<List<Object>> {

        private List<Class<?>> classList;
        private List<Object> beanList;

        public InstanceBeanTask(List<Class<?>> sourceClassList, List<Object> sourceBeanList) {
            classList = sourceClassList;
            beanList = sourceBeanList;
        }

        @Override
        public List<Object> call() {
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
            return beanList;
        }
    }
}
