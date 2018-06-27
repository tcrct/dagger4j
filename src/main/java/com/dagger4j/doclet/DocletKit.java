package com.dagger4j.doclet;

import com.dagger4j.db.annotation.Ignore;
import com.dagger4j.doclet.modle.ClassDocModle;
import com.dagger4j.doclet.modle.MethodDocModle;
import com.dagger4j.doclet.modle.ParameterModle;
import com.dagger4j.doclet.modle.TagModle;
import com.dagger4j.kit.PathKit;
import com.dagger4j.kit.PropKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.annotation.Controller;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.mvc.scan.ScanClassFactory;
import com.sun.javadoc.*;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by laotang
 * @date createed in 2018/6/27.
 */
public class DocletKit {

    private static List<Class<?>> controllerClassList;
    /**
     *  需要解析的Controller类， key为controller类， value为该controller类下的所有需要解析的方法集合
     *  需要解析是指类或方法没有@Ignore注解
     */
    private static Map<Class<?>, List<Method>> docletClassMap = new HashMap<>();
    // 文件名与Controller对应关系
    private static Map<String, Class<?>> docletFileMap = new HashMap<>();
    private static List<ClassDocModle> classDocModleList = new ArrayList<>();

    public static DocletKit duang() {
        return new DocletKit();
    }

    private DocletKit() {
        PropKit.use("dagger.properties");
//        controllerClassList = ClassHelper.getClontrllerClassList();
        if(ToolsKit.isEmpty(controllerClassList)) {
            init();
        }
        classDocModleList.clear();
    }

    private static void init() {
        String packagePath = PropKit.get(ConstEnums.BASE_PACKAGE_PATH.getValue());
        List<String> jarNames = PropKit.getList(ConstEnums.JAR_PREFIX.getValue());
        controllerClassList = ScanClassFactory.getAllClass(packagePath, jarNames, Controller.class);
        filterIgnore();
    }

    public void build() {
//        String rootPath = PathKit.getRootClassPath();
//        System.out.println(rootPath);
        String webPath = PathKit.getWebRootPath();
        File rootDir = new File(webPath).getParentFile();
        scanSourceFile(rootDir.getAbsolutePath());
        System.out.println(ToolsKit.toJsonString(classDocModleList));
    }

    /**
     * 如果类或方法还有Ignore注解，则过滤
     */
    private static void filterIgnore() {
        for(Class<?> controllerClass : controllerClassList) {
            if(!controllerClass.isAnnotationPresent(Ignore.class)) {
                Method[] methods = controllerClass.getMethods();
                List<Method> methodList = new ArrayList<>();
                for(Method method : methods) {
                    if(!method.getClass().isAnnotationPresent(Ignore.class)){
                        methodList.add(method);
                    }
                }
                docletClassMap.put(controllerClass, methodList);
                docletFileMap.put(controllerClass.getSimpleName(), controllerClass);
            }
        }
    }


    private static void scanSourceFile(String folderPath) {
        File dir = new File(folderPath);
        if (!dir.exists() || !dir.isDirectory()) {	//不存在或不是目录
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not exists or not is Directory!");
        }
        //遍历目录下的所有文件
        File[] files = dir.listFiles(ClassFileFilter(dir,".java"));
        for(int i=0; i<files.length; i++) {
            File file = files[i];
            if(file.isDirectory()){
                scanSourceFile(file.getAbsolutePath());
            }else{
                if(file.isFile()) {
                    String fileName = file.getName();
                    if(docletFileMap.containsKey(fileName.substring(0, fileName.length()-5))){
                        //调用解析方法
                        ClassDoc[] data = JavaDocReader.show(file.getPath());
                        ClassDoc classDoc = data[0];
                        // 如果抽象，静态， 接口则退出本次循环
                        if(ToolsKit.isEmpty(classDoc) || classDoc.isStatic() || classDoc.isAbstract() || classDoc.isInterface()) {
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
                        MethodDoc[]  methodDocs =  classDoc.methods();
                        if(ToolsKit.isNotEmpty(methodDocs)) {
                            methodDocModleList = new ArrayList<>(methodDocs.length);
                            for(MethodDoc methodDoc : methodDocs) {
                                // 是抽象方法,静态方法, 接口则退出
                                if(methodDoc.isAbstract() || methodDoc.isStatic() || methodDoc.isInterface()){
                                    continue;
                                }
                                MethodDocModle methodDocModle = new MethodDocModle();
                                // 方法名
                                methodDocModle.setName(methodDoc.name());
                                // 注释说明
                                methodDocModle.setCommentText(methodDoc.commentText().trim());
                                // 返回值
                                methodDocModle.setReturnType(methodDoc.returnType().typeName());
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
                                        ParameterModle parameterModle = new ParameterModle(parameter.typeName(), parameter.name(), "");
                                        parameterModleList.add(parameterModle);
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
                                methodDocModleList.add(methodDocModle);
                            }
                        }
                        classDocModleList.add(new ClassDocModle(classDoc.name(), classDoc.commentText().trim(), tagModleList, methodDocModleList));
                    }
                }
            }
        }
    }

    private static FileFilter ClassFileFilter(final File dir, final String extName){
        return new FileFilter() {
            public boolean accept(File file) {
                if(".java".equalsIgnoreCase(extName)) {
                    return ( file.isFile() && file.getName().toLowerCase().endsWith(extName) ) || file.isDirectory();
                }
                return false;
            }
        };
    }


}
