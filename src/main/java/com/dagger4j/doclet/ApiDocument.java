package com.dagger4j.doclet;

import com.dagger4j.db.annotation.Ignore;
import com.dagger4j.doclet.modle.ClassDocModle;
import com.dagger4j.doclet.modle.MethodDocModle;
import com.dagger4j.doclet.modle.ParameterModle;
import com.dagger4j.doclet.modle.TagModle;
import com.dagger4j.kit.ClassKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.annotation.Mapping;
import com.dagger4j.mvc.annotation.Mock;
import com.dagger4j.mvc.route.RequestMapping;
import com.dagger4j.utils.DataType;
import com.dagger4j.utils.GenericsUtils;
import com.dagger4j.vtor.annotation.NotEmpty;
import com.sun.javadoc.*;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Created by laotang
 * @date createed in 2018/6/27.
 */
public class ApiDocument {

    // 组装完成的List，返回到调用者
    private static List<ClassDocModle> classDocModleList = new ArrayList<>();
    // 源文件所有的第一级目录路径
    private String sourceDir;


    public ApiDocument(String sourceDir) {
        this.sourceDir = sourceDir;
    }


    public static List<ClassDocModle> getClassDocModleList() {
        return classDocModleList;
    }


    public List<ClassDocModle> document() {
        classDocModleList.clear();
        scanSourceFile(sourceDir);
        return classDocModleList;
    }

    private void scanSourceFile(String folderPath) {
        File dir = new File(folderPath);
        if (!dir.exists() || !dir.isDirectory()) {	//不存在或不是目录
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not exists or not is Directory!");
        }
        //遍历目录下的所有文件
        File[] files = dir.listFiles(classFileFilter(dir,".java"));
        for(int i=0; i<files.length; i++) {
            File file = files[i];
            if(file.isDirectory()){
                scanSourceFile(file.getAbsolutePath());
            }else{
                if(file.isFile()) {
                    if(!file.getName().toLowerCase().endsWith(".java")) {
                        continue;
                    }
                    //调用解析方法
                    ClassDoc[] data = JavaDocReader.show(file.getPath());
                    ClassDoc classDoc = data[0];
                    // 如果抽象，静态， 接口，设置了Ignore注解则退出本次循环
                    if(ToolsKit.isEmpty(classDoc) || isIgnoreAnnotation(classDoc.annotations())
                            || classDoc.isStatic() || classDoc.isAbstract() || classDoc.isInterface()) {
                        continue;
                    }

                    List<TagModle> tagModleList = null;
                    List<MethodDocModle> methodDocModleList = null;
                    Tag[] tagsArray = classDoc.tags();
                    if(ToolsKit.isNotEmpty(tagsArray)) {
                        tagModleList = new ArrayList<>(tagsArray.length);
                        for (Tag tag : tagsArray) {
                            if(ToolsKit.isNotEmpty(tag)) {
                                String tagName = tag.name();
                                tagModleList.add(new TagModle(tagName.substring(1, tagName.length()), tag.text()));
                            }
                        }
                    }
                    Method[] jdkMethod = null;
                    try {
                        Class<?> controllerClass = Class.forName(classDoc.toString());
                        jdkMethod = controllerClass.getDeclaredMethods();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Controller Mapping注解部份
                    RequestMapping mappingModle = buildRequestMapping(classDoc.annotations());

                    MethodDoc[]  methodDocs =  classDoc.methods();
                    if(ToolsKit.isNotEmpty(methodDocs)) {
                        methodDocModleList = new ArrayList<>(methodDocs.length);
                        for(MethodDoc methodDoc : methodDocs) {
                            // 是抽象方法,静态方法, 接口则退出
                            if(ToolsKit.isEmpty(methodDoc) || isIgnoreAnnotation(methodDoc.annotations())
                                    || methodDoc.isAbstract() || methodDoc.isStatic() || methodDoc.isInterface()){
                                continue;
                            }
                            MethodDocModle methodDocModle = new MethodDocModle();
                            // 方法名
                            methodDocModle.setName(methodDoc.name());
                            // 注释说明
                            methodDocModle.setCommentText(methodDoc.commentText().trim());
                            // 返回值
                            String returnTypeString = methodDoc.returnType().toString();
                            if(List.class.getName().equalsIgnoreCase(returnTypeString) ||
                                    Set.class.getName().equalsIgnoreCase(returnTypeString)  ) {
                                for(Method method : jdkMethod) {
                                    if(method.getName().equalsIgnoreCase(methodDoc.name())) {
                                        Class<?> typeClass = GenericsUtils.getGenericReturnType(method);
                                        returnTypeString = methodDoc.returnType().simpleTypeName()+"<"+typeClass.getTypeName()+">";
                                        System.out.println(returnTypeString);
                                    }
                                }
                            }
                            methodDocModle.setReturnType(returnTypeString);
                            // 异常部份
                            Type[] exceptionTypeArray = methodDoc.thrownExceptionTypes();
                            if(ToolsKit.isNotEmpty(exceptionTypeArray)) {
                                List<String> exceptionList = new ArrayList<>(exceptionTypeArray.length);
                                for(Type type : exceptionTypeArray) {
                                    exceptionList.add(type.typeName());
                                }
                                methodDocModle.setException(exceptionList);
                            }
                            //参数部份
                            Parameter[] parameters = methodDoc.parameters();
                            List<ParameterModle> parameterModleList = new ArrayList<>();
                            if(ToolsKit.isNotEmpty(parameters)) {
                                for(Parameter parameter : parameters) {
                                    /* 参数注解部份
                                    for(AnnotationDesc annotationDesc : parameter.annotations()) {
                                        AnnotationTypeDoc typeDoc = annotationDesc.annotationType();
                                        if(ToolsKit.isEmpty(typeDoc)) {
                                            continue;
                                        }
                                        System.out.println("################################: " + typeDoc.toString());

                                        for(AnnotationTypeElementDoc elementDocs : typeDoc.elements()){
                                            System.out.println("parameterName:  " +parameter.name());
                                            System.out.println("parameterType: " + parameter.type());
                                            System.out.println("elementName: " + elementDocs.name());

                                            annotationDesc..elementValues();
                                            try {
                                            // 要将Unicode转为中文
                                                System.out.println(URLDecoder.decode(elementDocs.defaultValue().toString(), "UTF-8"));
//                                            System.out.println("defaultValue:  " +new String(elementDocs.defaultValue().toString().getBytes(), "gb2312"));
                                            } catch (Exception e) {}
                                        }
                                    }
                                    */

                                    ParameterModle parameterModle = null;
                                    Type type = parameter.type();
                                    String typeString = type.toString();
                                    Class<?> parameterType = null;
                                    if(typeString.indexOf(".") == -1) {
                                        parameterType = ClassKit.loadClass(DataType.conversionBaseType(typeString));
                                    } else {
                                        parameterType = ClassKit.loadClass(type.toString());
                                    }
                                    if(ToolsKit.isDaggerBean(parameterType)) {
                                        Field[] fields = parameterType.getDeclaredFields();
                                        for(Field field : fields) {
                                            parameterModle = builderParameterModle(field);
                                            parameterModleList.add(parameterModle);
                                        }
                                    } else {
                                        parameterModle = new ParameterModle(parameter.typeName(), parameter.name(), "", true,"", "");
                                        builderParameterModle( parameterModle, parameter.annotations());
                                        parameterModleList.add(parameterModle);
                                    }
                                }
                                methodDocModle.setParamModles(parameterModleList);
                            }
                            // 注释部份
                            Tag[] tags = methodDoc.tags();
                            if(ToolsKit.isNotEmpty(tags)) {
                                List<TagModle> modlesTagList = new ArrayList<>();
                                for (Tag tag : tags) {
                                    String tagName = tag.name();
                                    modlesTagList.add(new TagModle(tagName.substring(1, tagName.length()), tag.text()));
                                }
                                methodDocModle.setTagModles(modlesTagList);
                            }
                            // 注解部份
                            RequestMapping methodMapping = buildRequestMapping(methodDoc.annotations());
                            methodDocModle.setMappingModle(methodMapping);

                            methodDocModleList.add(methodDocModle);
                        }
                    }
                    classDocModleList.add(new ClassDocModle(classDoc.toString(), mappingModle, classDoc.commentText().trim(), tagModleList, methodDocModleList));
                }
            }
        }
    }

    /**
     * 过滤文件
     * @param dir
     * @param extName
     * @return
     */
    private FileFilter classFileFilter(final File dir, final String extName){
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(".java".equalsIgnoreCase(extName)) {
                    return ( file.isFile() && file.getName().toLowerCase().endsWith(extName) ) || file.isDirectory();
                }
                return false;
            }
        };
    }

    private boolean isIgnoreAnnotation(AnnotationDesc[] annotatedDescArray) {
        if(ToolsKit.isNotEmpty(annotatedDescArray)) {
            for(AnnotationDesc annotation : annotatedDescArray) {
                // 这里注解能拿出注解里的值，名称，返回类型等信息
//                System.out.println(annotation.elementValues()[0].element().returnType().toString()+"             "+annotation.elementValues()[0].value());
//                System.out.println(annotation.annotationType().toString()+"                 "+Ignore.class.getName());
                if(annotation.annotationType().toString().equalsIgnoreCase(Ignore.class.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 取得controller里的mapping注解信息
     * @param annotatedDescArray
     * @return
     */
    private RequestMapping buildRequestMapping(AnnotationDesc[] annotatedDescArray) {
        RequestMapping requestMapping = new RequestMapping();
        if(ToolsKit.isNotEmpty(annotatedDescArray)) {
            for(AnnotationDesc annotation : annotatedDescArray) {
                // 这里注解能拿出注解里的值，名称，返回类型等信息
                if(annotation.annotationType().toString().equalsIgnoreCase(Mapping.class.getName())) {
                    AnnotationDesc.ElementValuePair[] elementValuePairs = annotation.elementValues();
                    for(AnnotationDesc.ElementValuePair pair : elementValuePairs) {
                        AnnotationTypeElementDoc elementDoc = pair.element();
                        AnnotationValue annotationValue = pair.value();
                        Object valueObj = annotationValue.value();
//                        System.out.println(valueObj.toString());
                        if(RequestMapping.VALUE_FIELD.equalsIgnoreCase(elementDoc.name())) {
                            requestMapping.setValue(valueObj.toString());
                        }
                        if(RequestMapping.DESC_FIELD.equalsIgnoreCase(elementDoc.name())) {
                            requestMapping.setDesc(valueObj.toString());
                        }
                        if(RequestMapping.METHOD_FIELD.equalsIgnoreCase(elementDoc.name())) {
                            StringBuilder methodString = new StringBuilder();
                            if(annotationValue.value() instanceof Object[]) {
                                AnnotationValue[] methods = (AnnotationValue[])annotationValue.value();
                                if(ToolsKit.isNotEmpty(methods)) {
                                    for (AnnotationValue httpMethod : methods) {
                                        String method = httpMethod.toString();
                                        method = method.substring(method.lastIndexOf(".")+1, method.length());
                                        methodString.append(method).append(",");
                                    }
                                    if(methodString.length()>-1) {
                                        methodString.deleteCharAt(methodString.length()-1);
                                    }
                                }
                            }
//                            System.out.println(annotationValue.toString());
                            requestMapping.setMethod(methodString.toString());
                        }

                        if(RequestMapping.ORDER_FIELD.equalsIgnoreCase(elementDoc.name())) {
                            int order = 3000;
                            try {
                                if(ToolsKit.isNotEmpty(valueObj)) {
                                    order = Integer.parseInt(valueObj.toString());
                                }
                            } catch (Exception e) {}
                            requestMapping.setOrder(order);
                        }
                        if(RequestMapping.TIMEOUT_FIELD.equalsIgnoreCase(elementDoc.name())) {
                            int timeout = 3000;
                            try {
                                if(ToolsKit.isNotEmpty(valueObj)) {
                                    timeout = Integer.parseInt(valueObj.toString());
                                }
                            } catch (Exception e) {}
                            requestMapping.setTimeout(timeout);
                        }
                    }
//                    System.out.println(annotation.elementValues()[0].element().returnType().toString()+"             "+annotation.elementValues()[0].value());
//                    System.out.println(annotation.annotationType().toString()+"                 "+Mapping.class.getName());
//                    System.out.println(requestMapping.getValue()+"                 "+requestMapping.getDesc());
                }
            }
        }
        return requestMapping;
    }

    private ParameterModle builderParameterModle(Field field) {
        ParameterModle parameterModle = new ParameterModle();
        parameterModle.setName(field.getName());
        parameterModle.setType(field.getType().getSimpleName());
        Annotation[] annotations = field.getAnnotations();
        if(ToolsKit.isNotEmpty(annotations)) {
            for(Annotation annotation : annotations) {
                Class<?> annotationClass = annotation.annotationType();
//                System.out.println(field.getName() + "##########: " + annotationClass.getName() );
                if(NotEmpty.class.equals(annotationClass)) {
                    parameterModle.setEmpty(false);  // no empty
                }
                if(Mock.class.equals(annotationClass)) {
                    Mock mock = field.getAnnotation(Mock.class);
                    parameterModle.setDefaultValue(mock.value());
                    parameterModle.setDesc(mock.desc());
                }
//                builderParameterModleItem(parameterModle, annotationClass);
            }
        }
        return parameterModle;
    }

    private void builderParameterModle(ParameterModle parameterModle, AnnotationDesc[] annotatedDescArray) {
        if(ToolsKit.isNotEmpty(annotatedDescArray)) {
            for(AnnotationDesc annotation : annotatedDescArray) {
                // 这里注解能拿出注解里的值，名称，返回类型等信息
                String typeString = annotation.annotationType().toString();
                AnnotationDesc.ElementValuePair[] pairs = annotation.elementValues();
                if(ToolsKit.isNotEmpty(pairs)) {
                    for(AnnotationDesc.ElementValuePair pair : pairs) {
                        if(NotEmpty.class.getName().equalsIgnoreCase(typeString)) {
                            parameterModle.setEmpty(false);
                        }
                        if("defaultValue".equalsIgnoreCase(pair.element().name())) {
                            parameterModle.setDefaultValue(pair.value().value().toString());
                        }
                    }
                }
            }
        }
    }

//    private void builderParameterModleItem(ParameterModle parameterModle , Class<?> annotationClass) {
//        if(NotEmpty.class.equals(annotationClass)) {
//            parameterModle.setEmpty(false);  // no empty
//        }
//        if(Mock.class.equals(annotationClass)) {
//            Mock mock = field.getAnnotation(Mock.class);
//            parameterModle.setDefaultValue(mock.value());
//        }
//    }
}
