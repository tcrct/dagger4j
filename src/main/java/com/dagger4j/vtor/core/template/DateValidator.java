package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.vtor.annotation.constraints.Date;

import java.lang.annotation.Annotation;

/**
 * html转义验证处理器 ???
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class DateValidator extends AbstractValidatorTemplate<Date> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Date.class;
    }

    @Override
    public void handle(Date annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }

        try {
            ToolsKit.parseDate(paramValue.toString(), annonation.format());
        } catch (Exception e) {
            try {
                ToolsKit.parseDate(paramValue.toString(), ConstEnums.DEFAULT_DATE_FORMAT_VALUE.getValue());
            } catch (Exception e1) {
                throw new ValidatorException(paramName + annonation.message());
            }
        }

    }
}
