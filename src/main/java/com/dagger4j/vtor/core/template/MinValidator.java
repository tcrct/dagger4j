package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.vtor.annotation.constraints.Min;

import java.lang.annotation.Annotation;

/**
 * 最小值验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class MinValidator extends AbstractValidatorTemplate<Min> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Min.class;
    }

    @Override
    public void handle(Min annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }
        try {
          if(Double.parseDouble(paramValue+"") < annonation.value()){
              throw new ValidatorException(paramName+"["+paramValue+"]"+annonation.message().replace("${value}", annonation.value()+""));
          }
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage());
        }
    }
}
