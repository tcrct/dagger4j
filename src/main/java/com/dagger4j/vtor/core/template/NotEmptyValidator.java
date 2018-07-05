package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.vtor.annotation.NotEmpty;

import java.lang.annotation.Annotation;

/**
 * 不为空验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class NotEmptyValidator extends AbstractValidatorTemplate<NotEmpty> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return NotEmpty.class;
    }

    @Override
    public void handle(NotEmpty annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName+"["+paramValue+"]"+ annonation.message());
        }
    }
}
