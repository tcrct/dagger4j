package com.dagger4j.kit;

import com.dagger4j.exception.MvcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by laotang
 * @date createed in 2018/6/14.
 */
public final class ClassKit {

    private static final Logger logger = LoggerFactory.getLogger(ClassKit.class);


    // entity class pool
    private static final Map<String, Class<?>> entityClassMap = new HashMap<>();
    // othor class pool
    private static final Map<String, Class<?>> othorClassMap = new HashMap<>();

    public static ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : ClassKit.class.getClassLoader();
    }

    /**
     * 实例化类文件, 默认实例化
     * @param clazz  类文件
     * @return
     */
    public static Class<?> loadClass(Class<?> clazz) {
        return loadClass(clazz, true);
    }
    /**
     * 实例化类文件
     * @param clazz  类文件
     * @param isInit  是否实例
     * @return
     */
    public static Class<?> loadClass(Class<?> clazz, boolean isInit) {
        if (ToolsKit.isEmpty(clazz)) {
            return null;
        }
        try {
            // 要初始化且支持实例化
            if(isInit && supportInstance(clazz)) {
                clazz = Class.forName(clazz.getName(), isInit, ClassKit.getClassLoader());
            }
        } catch (ClassNotFoundException e) {
            logger.warn("Load class is error:" + clazz.getName(), e);
            throw new MvcException(e.getMessage(), e);
        } catch (Exception e) {
            logger.warn("Load class is error:" + clazz.getName(), e);
            throw new MvcException(e.getMessage(), e);
        }
        return clazz;
    }


    /**
     * 检查该类是否支持实例化
     * <p>如果是抽象类或接口类则返回false</p>
     *
     * @param clazz
     * @return
     */
    public static boolean supportInstance(Class<?> clazz) {
        if(null == clazz) {
            return false;
        }
        //
        if(Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
            return false;
        }
//        Controller controller = clazz.getAnnotation(Controller.class);
//        if(null != controller && !controller.autowired()){
//            return false;
//        }
//        Service service = clazz.getAnnotation(Service.class);
//        if(null != service && !service.autowired()){
//            return false;
//        }
        return true;
    }

//    public static List<Class<?>> getAllClass(String packagePath, List<String> jarNames) {
//        List<Class<?>> classList = allClassMap.get(packagePath);
//        if(ToolsKit.isEmpty(classList)) {
//           classList = ScanClassFactory.scanClass(packagePath, jarNames);
//            if(ToolsKit.isNotEmpty(classList))  {
//                allClassMap.put(packagePath, classList);
//            }
//        }
//        return classList;
//    }


}
