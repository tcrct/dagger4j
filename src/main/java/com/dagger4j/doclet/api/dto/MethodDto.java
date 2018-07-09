package com.dagger4j.doclet.api.dto;

/**
 * @author Created by laotang
 * @date createed in 2018/7/9.
 */
public class MethodDto {

    private String name;
    private String desc;
    private String method;

    public MethodDto() {
    }

    public MethodDto(String name, String desc, String method) {
        this.name = name;
        this.desc = desc;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
