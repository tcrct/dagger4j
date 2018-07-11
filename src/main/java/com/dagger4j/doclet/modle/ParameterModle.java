package com.dagger4j.doclet.modle;

/**
 * 参数体文件模型
 * @author Created by laotang
 * @date createed in 2018/6/27.
 */
public class ParameterModle {
    // 参数类型
    private String type;
    // 参数名称
    private String name;
    // mock数据值
    private String mock;
    // 是否必填(必填为true)
    private boolean empty;
    // 验证规则字符串
    private String rules;

    public ParameterModle() {

    }

    public ParameterModle(String type, String name, String mock, boolean empty, String rules) {
        this.type = type;
        this.name = name;
        this.mock = mock;
        this.empty = empty;
        this.rules = rules;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getMock() {
        return mock;
    }

    public void setMock(String mock) {
        this.mock = mock;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public String toString() {
        return "ParameterModle{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", mock='" + mock + '\'' +
                ", empty=" + empty +
                ", rules='" + rules + '\'' +
                '}';
    }
}
