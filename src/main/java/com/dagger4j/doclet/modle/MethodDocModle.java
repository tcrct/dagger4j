package com.dagger4j.doclet.modle;

import java.util.List;

/**
 * 类方法文档模型
 * @author Created by laotang
 * @date createed in 2018/6/27.
 */
public class MethodDocModle implements java.io.Serializable {

    // 方法名
    private String name;
    // 注释说明
    private String commentText;
    //异常信息
    private List<String> exception;
    //返回值类型
    private String returnType;
    // 注释体  /**  */ 里的内容
    private List<TagModle> tagModles;
    //参数体
    private List<ParameterModle> paramModles;

    public MethodDocModle() {

    }

    public MethodDocModle(String name, String commentText, List<String> exception, String returnType, List<TagModle> tagModles, List<ParameterModle> paramModles) {
        this.name = name;
        this.commentText = commentText;
        this.exception = exception;
        this.returnType = returnType;
        this.tagModles = tagModles;
        this.paramModles = paramModles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public List<String> getException() {
        return exception;
    }

    public void setException(List<String> exception) {
        this.exception = exception;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<TagModle> getTagModles() {
        return tagModles;
    }

    public void setTagModles(List<TagModle> tagModles) {
        this.tagModles = tagModles;
    }

    public List<ParameterModle> getParamModles() {
        return paramModles;
    }

    public void setParamModles(List<ParameterModle> paramModles) {
        this.paramModles = paramModles;
    }

    @Override
    public String toString() {
        return "MethodDocModle{" +
                "name='" + name + '\'' +
                ", exception=" + exception +
                ", returnType='" + returnType + '\'' +
                ", tagModles=" + tagModles +
                ", paramModles=" + paramModles +
                '}';
    }
}
