package com.dagger4j.mvc.core;

import com.dagger4j.exception.MvcException;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.utils.DataType;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Created by laotang
 * @date createed in 2018/6/28.
 */
public class MethodParameterNameDiscoverer {

    /**
     * 要过滤的方法(Object自带方法及BaseController里的方法)
     */
    private static final Set<String> excludedMethodName = ObjectKit.buildExcludedMethodName(BaseController.class);
    private static final Map<String, String[]> parameterNamePool = new ConcurrentHashMap<>();

    public static String[]  getParameterNames(IRequest request, Class<?> clazz, Method method) {
        String key = buildParameterNamePoolKey(clazz, method);
        String[] parameters = parameterNamePool.get(key);
        if(ToolsKit.isEmpty(parameters)) {
            Method[] methodArray = clazz.getMethods();
            for (Method itemMethod : methodArray) {
                if (!ObjectKit.isNormalMethod(itemMethod.getModifiers()) ||
                        excludedMethodName.contains(itemMethod.getName())) {
                    continue;
                }
                String parameterKey = buildParameterNamePoolKey(clazz, itemMethod);
                String[] paramNameArray = getParameterNamesByAsm(clazz, itemMethod);

                Parameter[] actionParams = method.getParameters();
                if (ToolsKit.isNotEmpty(actionParams)) {
                    if(actionParams.length != paramNameArray.length) {
                        throw new MvcException("参数长度不一致!");
                    }
                    Object[] requestParamValueObj = new Object[actionParams.length];
                    for(int i=0; i<actionParams.length; i++) {
                        Class<?> parameterType = actionParams[i].getType();
                        Annotation[]   annotations = actionParams[i].getAnnotations();
                        if(ToolsKit.isNotEmpty(annotations)) {
                            for(Annotation annotation : annotations) {
                                System.out.println(annotation.annotationType() + "                      " + parameterType.getName() + "                  " + paramNameArray[i]);
                                String paramValue = request.getParameter(paramNameArray[i]);
                                if(DataType.isString(parameterType)) {
                                    requestParamValueObj[i] = paramValue;
                                } else if(DataType.isInteger(parameterType) && DataType.isIntegerObject(parameterType)) {
                                    requestParamValueObj[i] = Integer.parseInt(paramValue);
                                } else if(DataType.isLong(parameterType) && DataType.isLongObject(parameterType)) {
                                    requestParamValueObj[i] = Long.parseLong(paramValue);
                                }


                            }
                        }
                    }
                }

                if (ToolsKit.isNotEmpty(paramNameArray)) {
                    parameterNamePool.put(parameterKey, paramNameArray);
                }
            }
        }
        if(!parameterNamePool.containsKey(key)) {
            throw new MvcException("取方法参数体时异常，参数获取失败!");
        }
        return parameterNamePool.get(key);
    }

    /**
     * 创建缓存KEY， 类全路径+方法名，来标识唯一
     * 因为请求API是唯一的，所以在Controller里不会出一致的方法名，所以就没做进一步的唯一性确定处理
     * 如果需要加强唯一性，可以将方法体里的参数类型取出，再拼接字符串后MD5
     * @param clazz
     * @param method
     * @return
     */
    private static String buildParameterNamePoolKey(Class<?> clazz, Method method) {
        return clazz.getName() +"."+ method.getName(); //+"#"+method.getReturnType().getName();
    }

    /**
     *
     * @param clazz
     * @param method
     * @return
     */
    private static String[] getParameterNamesByAsm(Class<?> clazz, final Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (ToolsKit.isEmpty(parameterTypes)) {
            return null;
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];

        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = clazz.getResourceAsStream(className);
        try {
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override
                public MethodVisitor visitMethod(int access, String name,
                                                 String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return super.visitMethod(access, name, desc, signature,
                                exceptions);
                    }
                    return new MethodVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitLocalVariable(String name, String desc,
                                                       String signature, org.objectweb.asm.Label start,
                                                       org.objectweb.asm.Label end, int index) {
                            // 非静态成员方法的第一个参数是this
                            if (Modifier.isStatic(method.getModifiers())) {
                                parameterNames[index] = name;
                            } else if (index > 0) {
                                if(parameterNames.length == 1) {
                                    // 如果是只有一个参数时，该index为2， name为e，ams的bug???
                                    parameterNames[0] = name;
                                } else {
                                    parameterNames[index - 1] = name;
                                }
                            }
                        }
                    };
                }
            }, 0);
        } catch (IOException e) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e2) {
            }
        }
        return parameterNames;
    }


}
