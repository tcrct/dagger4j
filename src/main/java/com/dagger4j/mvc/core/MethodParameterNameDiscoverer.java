package com.dagger4j.mvc.core;

import com.dagger4j.kit.ObjectKit;
import com.dagger4j.kit.ToolsKit;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
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

    public static String[]  builderParameterObject(Class<?> clazz, Method method) {
        Parameter[] actionParams = method.getParameters();
        if(ToolsKit.isEmpty(actionParams)) {
            return null;
        }
        for(int i=0; i<actionParams.length; i++) {
            Parameter parameter = actionParams[i];
            // 参数类型
            Class<?> parameterType = actionParams[i].getType();
            String[] paramNameArray = getParameterNamesByAsm(clazz, method);
        }
    }

    public static String[]  getParameterNames(Class<?> clazz, Method method) {
        String[] parameters = parameterNamePool.get(buildParameterNamePoolKey(clazz, method));
        if(ToolsKit.isEmpty(parameters)) {
            Method[] methodArray = clazz.getMethods();
            for (Method itemMethod : methodArray) {
                if (!ObjectKit.isNormalMethod(itemMethod.getModifiers()) ||
                        excludedMethodName.contains(itemMethod.getName())) {
                    continue;
                }
                String parameterKey = buildParameterNamePoolKey(clazz, itemMethod);
                String[] paramNameArray = getParameterNamesByAsm(clazz, itemMethod);
                if (ToolsKit.isNotEmpty(paramNameArray)) {
                    parameterNamePool.put(parameterKey, paramNameArray);
                    parameters = paramNameArray;
                }
            }
        }
        return parameters;
    }

    /**
     * 创建缓存KEY， 类全路径+方法名，来标识唯一
     * @param clazz
     * @param method
     * @return
     */
    private static String buildParameterNamePoolKey(Class<?> clazz, Method method) {
        return clazz.getName() +"."+ method.getName()+"#"+method.getReturnType().getName();
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
