package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.vtor.annotation.DaggerId;

import java.lang.annotation.Annotation;

/**
 * 邮箱验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class DaggerIdValidator extends AbstractValidatorTemplate<DaggerId> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return DaggerId.class;
    }

    @Override
    public void handle(DaggerId annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }
        if(!ToolsKit.isValidDaggerId(paramValue.toString())) {
            throw new ValidatorException(paramName+"["+paramValue+"]"+annonation.message());
        }
    }
}
