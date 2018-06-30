package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.vtor.annotation.constraints.Length;

import java.lang.annotation.Annotation;

/**
 * 长度验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class LengthValidator extends AbstractValidatorTemplate<Length> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Length.class;
    }

    @Override
    public void handle(Length annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }
        if(paramValue.toString().length() > annonation.value()) {
            throw new ValidatorException(paramName+annonation.message().replace("${value}", annonation.value()+""));
        }
    }
}
