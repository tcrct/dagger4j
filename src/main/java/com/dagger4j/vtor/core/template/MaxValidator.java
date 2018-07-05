package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.vtor.annotation.Max;

import java.lang.annotation.Annotation;

/**
 * 最大值验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class MaxValidator extends AbstractValidatorTemplate<Max> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Max.class;
    }

    @Override
    public void handle(Max annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }
        try {
          if(Double.parseDouble(paramValue+"") > annonation.value()){
              throw new ValidatorException(paramName+"["+paramValue+"]"+annonation.message().replace("${value}", annonation.value()+""));
          }
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage());
        }
    }
}
