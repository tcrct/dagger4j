package com.dagger4j.doclet;

import com.dagger4j.db.annotation.Ignore;
import com.dagger4j.doclet.modle.ClassDocModle;
import com.dagger4j.doclet.modle.MethodDocModle;
import com.dagger4j.doclet.modle.ParameterModle;
import com.dagger4j.doclet.modle.TagModle;
import com.dagger4j.kit.ToolsKit;
import com.sun.javadoc.*;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Created by laotang
 * @date createed in 2018/6/27.
 */
public class ApiDocument {

    // 组装完成的List，返回到调用者
    private List<ClassDocModle> classDocModleList = new ArrayList<>();
    // 源文件所有的第一级目录路径
    private String sourceDir;


    public ApiDocument(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String document() {
        scanSourceFile(sourceDir);
        return ToolsKit.toJsonString(classDocModleList);
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
                    classDocModleList.add(new ClassDocModle(classDoc.toString(), classDoc.commentText().trim(), tagModleList, methodDocModleList));
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

}
