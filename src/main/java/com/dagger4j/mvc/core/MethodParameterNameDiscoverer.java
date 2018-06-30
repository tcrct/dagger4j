package com.dagger4j.mvc.core;

import com.dagger4j.db.annotation.IdEntity;
import com.dagger4j.exception.MvcException;
import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.utils.DataType;
import com.dagger4j.vtor.core.VtorFactory;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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

    public static String[]  getParameterNames(Class<?> clazz, Method method) throws Exception{
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
                String[] paramNameArray = getMethodParamNames(itemMethod);
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
     * 将请求参数转换为Object[], 注入到Method里
     * @param request       请求对象
     * @param method       执行的方法
     * @param paramNameArray        执行方法里的参数变量名
     * @return
     */
    public static Object[] getParameterValues(IRequest request, Method method, String[] paramNameArray)  {
        Parameter[] actionParams = method.getParameters();
        Object[] requestParamValueObj = null;
        if (ToolsKit.isNotEmpty(actionParams)) {
            if(actionParams.length != paramNameArray.length) {
                throw new MvcException("参数长度不一致!");
            }
            requestParamValueObj = new Object[actionParams.length];
            for(int i=0; i<actionParams.length; i++) {
                Class<?> parameterType = actionParams[i].getType();
                String paramValue = request.getParameter(paramNameArray[i]);
                if(DataType.isString(parameterType)) {
                    requestParamValueObj[i] = paramValue;
                } else if(DataType.isInteger(parameterType) || DataType.isIntegerObject(parameterType)) {
                    requestParamValueObj[i] = Integer.parseInt(paramValue);
                } else if(DataType.isLong(parameterType) || DataType.isLongObject(parameterType)) {
                    requestParamValueObj[i] = Long.parseLong(paramValue);
                } else if(DataType.isDouble(parameterType) || DataType.isDoubleObject(parameterType)) {
                    requestParamValueObj[i] = Double.parseDouble(paramValue);
                } else if(DataType.isDate(parameterType)) {
                    requestParamValueObj[i] = ToolsKit.parseDate(paramValue, ConstEnums.DEFAULT_DATE_FORMAT_VALUE.getValue());
                } else if(DataType.isTimestamp(parameterType)) {
                    requestParamValueObj[i] = ToolsKit.parseDate(paramValue, ConstEnums.DEFAULT_DATE_FORMAT_VALUE.getValue());

                }
                // 如果是Entity，则认为是要转换为Bean对象
                else if(DataType.isIdEntityType(parameterType)){
                    String json = request.getParameter(ConstEnums.INPUTSTREAM_STR_NAME.getValue());
                    IdEntity entity = (IdEntity)ToolsKit.jsonParseObject(json, parameterType);
                    requestParamValueObj[i] = entity;
                    // 如果Bean的话，无需在参数添加注解，遍历bean里的field进行判断是否需要验证
                    try {
                        VtorFactory.validator(entity);
                    } catch (Exception e) {
                        throw new ValidatorException(e.getMessage(), e);
                    }
                }
                //返回前，根据验证注解，进行参数数据验证
                Annotation[]   annotations = actionParams[i].getAnnotations();
                if(ToolsKit.isNotEmpty(annotations)) {
                    try {
                        for (Annotation annotation : annotations) {
                            System.out.println(annotation.annotationType() + "                      " + parameterType.getName() + "                  " + paramNameArray[i] + "              " + paramValue);
                            VtorFactory.validator(annotation, parameterType, paramNameArray[i], paramValue);
                        }
                    } catch (Exception e) {
                        throw new ValidatorException(e.getMessage(), e);
                    }
                }
            }
        }
//        System.out.println(requestParamValueObj);

//        VtorKit.validate(requestaramValueObj);
        return requestParamValueObj;
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
     * 比较参数类型是否一致
     * @param types
     *            asm的类型({@link Type})
     * @param clazzes
     *            java 类型({@link Class})
     * @return
     */
    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
        // 个数不同
        if (types.length != clazzes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(clazzes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取方法的参数名
     只取所需的参数值, 因为所取得的index是无序的，到目前简单测试，得出的排序规则是如果出现了this(一般对应的index是0)后，
     后面顺延的索引位对应的值就是参数的名称，但这个顺延的索引数据有可能并不是按正序排序的，所以先存在TreeMap里排好序
     再将排序后的位置去掉第一位元素后，再方法参数的总长度，从第二位元素顺开始顺延取出参数名。
     如：
             Controller里的方法save(String id, String name, String address)
             ASM后，取出的内容可能是
             name          index
             e                 8
             value           7
             this            0
             id              1
             name         2
             address     3
             null           5
     TreeMap排序后就是0123578，将0去掉，往后再取3位(方法参数长度是3)元素的值就是方法体参数对应的参数名
     * @param m
     * @return
     */
    public static String[] getMethodParamNames(final Method m) {
        final String[] paramNames = new String[m.getParameterTypes().length];
        final String n = m.getDeclaringClass().getName();
        ClassReader cr = null;
        try {
            cr = new ClassReader(n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TreeMap<Integer, String> itemVisitorNameMap = new TreeMap<>();
        cr.accept(new ClassVisitor(Opcodes.ASM4) {
            @Override
            public MethodVisitor visitMethod(final int access,
                                             final String name, final String desc,
                                             final String signature, final String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(desc);
                // 方法名相同并且参数个数相同
                if (!name.equals(m.getName())
                        || !sameType(args, m.getParameterTypes())) {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }

                MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM4, v) {
                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        itemVisitorNameMap.put(index, name);
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }
                };
            }
        }, 0);
        Collection<String> nameList = itemVisitorNameMap.values();
        String[] filertParamNames = nameList.toArray(new String[]{});
        // i+1 : 如果是非静态方法，第一位的值就是this, 要将第一位元素过滤掉
        for(int i=0; i<paramNames.length; i++){
            paramNames[i] =  filertParamNames[i+1];
        }
        return paramNames;
    }
}
