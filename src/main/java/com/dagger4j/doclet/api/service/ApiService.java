package com.dagger4j.doclet.api.service;

import com.dagger4j.doclet.ApiDocument;
import com.dagger4j.doclet.api.dto.MethodDto;
import com.dagger4j.doclet.api.dto.MethodListDto;
import com.dagger4j.doclet.modle.ClassDocModle;
import com.dagger4j.doclet.modle.MethodDocModle;
import com.dagger4j.exception.MvcException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.route.RequestMapping;

import java.util.*;

/**
 * @author Created by laotang
 * @date createed in 2018/7/9.
 */
public class ApiService {

    // Controller Api 列表 key为Controller name
    private static final Map<String, String> controllerListMap = new TreeMap<>();
    // Controller Method 列表 key为Controller name
    private static final Map<String, MethodListDto> methodListMap = new TreeMap<>();
    // Method 详细 key为Controller name+"."+Method name
    private static final Map<String, MethodDocModle> methodDetailMap = new TreeMap<>();

    public Map<String, String> list(String product) {
        if(ToolsKit.isNotEmpty(controllerListMap)) {
            return controllerListMap;
        }
        List<ClassDocModle> classDocModleList = ApiDocument.getClassDocModleList();
        if(ToolsKit.isEmpty(classDocModleList)) {
            throw new MvcException("请先生成api文档");
        }

        for(Iterator<ClassDocModle> it = classDocModleList.iterator(); it.hasNext();) {
            ClassDocModle docModle = it.next();
            RequestMapping mapping = docModle.getMappingModle();
            String key = docModle.getName();
            String desc = ToolsKit.isEmpty(mapping.getDesc()) ? docModle.getName() : mapping.getDesc();
            controllerListMap.put(key, desc);
            MethodListDto methodListDto = new MethodListDto();
            methodListDto.setControllerDesc(desc);
            methodListDto.setControllerReadme(docModle.getCommentText());
            List<MethodDocModle> methodDocModleList = docModle.getMethods();
            if(ToolsKit.isNotEmpty(methodDocModleList)) {
                List<MethodDto> methodDtoList = new ArrayList<>(methodDocModleList.size());
                for(MethodDocModle methodDocModle : methodDocModleList) {
                    MethodDto methodDto = new MethodDto();
                    RequestMapping requestMapping = methodDocModle.getMappingModle();
                    String name = methodDocModle.getName();
                    desc = name;
                    String method = "";
                    if(ToolsKit.isNotEmpty(requestMapping)) {
                        name = requestMapping.getValue();
                    }
                    if(ToolsKit.isNotEmpty(requestMapping)) {
                        desc = requestMapping.getDesc();
                    }
                    if(ToolsKit.isNotEmpty(requestMapping) && ToolsKit.isNotEmpty(requestMapping.getMethod())) {
                        method = requestMapping.getMethod();
                    }
                    methodDto.setName(name);
                    methodDto.setDesc(desc);
                    methodDto.setMethod(method);
                    methodDtoList.add(methodDto);
                    methodDetailMap.put(key+"."+name, methodDocModle);
                }
                methodListDto.setMethodDtoList(methodDtoList);
            }
            methodListMap.put(key, methodListDto);
        }
        return controllerListMap;
    }

    /**
     * 返回方法列表DTO
     * @param key       Controller Name
     * @return
     */
    public MethodListDto methodList(String key) {
        if(ToolsKit.isEmpty(methodListMap) || !methodListMap.containsKey(key)) {
            throw new MvcException("methodListMap is null");
        }
        return methodListMap.get(key);
    }

    /**
     * 返回方法详细DTO
     * @param key       Controller Name+"."+Method Name
     * @return
     */
    public MethodDocModle methodDetail(String key) {
        if(ToolsKit.isEmpty(methodDetailMap) || !methodDetailMap.containsKey(key)) {
            throw new MvcException("methodListMap is null or key["+key+"] not exist");
        }
        return methodDetailMap.get(key);
    }

}
