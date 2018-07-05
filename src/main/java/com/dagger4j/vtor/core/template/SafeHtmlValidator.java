package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.vtor.annotation.SafeHtml;

import java.lang.annotation.Annotation;

/**
 * html转义验证处理器 ???
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class SafeHtmlValidator extends AbstractValidatorTemplate<SafeHtml> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return SafeHtml.class;
    }

    @Override
    public void handle(SafeHtml annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }
        paramValue = ToolsKit.toHTMLChar(paramValue.toString());
    }
}
