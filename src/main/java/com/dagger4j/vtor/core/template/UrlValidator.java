package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.PatternKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.vtor.annotation.constraints.URL;

import java.lang.annotation.Annotation;

/**
 * URL验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class UrlValidator extends AbstractValidatorTemplate<URL> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return URL.class;
    }

    @Override
    public void handle(URL annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }
        boolean isUrl = false;
        if("*".equals(annonation.regexp())) {
            isUrl = PatternKit.isURL(paramValue+"");
        } else {
            isUrl = PatternKit.isMatch(annonation.regexp(), paramValue+"");
        }
        if(!isUrl) {
            throw new ValidatorException(paramName+"["+paramValue+"]"+annonation.message());
        }
    }
}
