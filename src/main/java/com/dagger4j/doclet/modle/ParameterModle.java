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
    // 验证规则字符串
    private String rules;

    public ParameterModle(String type, String name, String rules) {
        this.type = type;
        this.name = name;
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

    @Override
    public String toString() {
        return "ParameterModle{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", rules='" + rules + '\'' +
                '}';
    }
}
