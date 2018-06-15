package com.dagger4j.kit;

import com.dagger4j.exception.MvcException;
import com.dagger4j.mvc.http.enums.ConstEnums;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by laotang
 * @date createed in 2018/6/14.
 */
public final class ClassKit {

    // controller class pool
    private static final Map<String,Class<?>> clontrllerClassMap = new HashMap<>();
    // service class pool
    private static final Map<String,Class<?>> serviceClassMap = new HashMap<>();
    // entity class pool
    private static final Map<String,Class<?>> entityClassMap = new HashMap<>();
    // othor class pool
    private static final Map<String,Class<?>> othorClassMap = new HashMap<>();

    /**
     * 根据指定的包路径，取出所有在该路径下的类
     * @param packagePath
     * @return
     */
    public static Map<String,Class<?>> getAllClass(String packagePath) {
        Enumeration<URL> urlEnumeration = PathKit.getPaths(packagePath);
        if(ToolsKit.isEmpty(urlEnumeration)) {
            throw new MvcException("根据["+packagePath+"]路径取类时出错，该路径下没有类返回");
        }
        while (urlEnumeration.hasMoreElements()) {
            URL classUrl = urlEnumeration.nextElement();
            if(ToolsKit.isEmpty(classUrl)) {
                continue;
            }
            String protocol = classUrl.getProtocol();
            if(ConstEnums.CLASS_URL_PROTOCOL_FILE_FIELD.getValue().equalsIgnoreCase(protocol)) {
                String calssPath = classUrl.getPath().replaceAll("%20", " ");
                String classFile = classUrl.getFile();

            } else if (ConstEnums.CLASS_URL_PROTOCOL_JAR_FIELD.getValue().equalsIgnoreCase(protocol)) {

            }
        }
    }

}
