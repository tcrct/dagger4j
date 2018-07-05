package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.utils.DataType;
import com.dagger4j.vtor.annotation.Ymd;

import java.lang.annotation.Annotation;

/**
 * html转义验证处理器 ???
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class YmdValidator extends AbstractValidatorTemplate<Ymd> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Ymd.class;
    }

    @Override
    public void handle(Ymd annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        if(ToolsKit.isEmpty(paramValue)) {
            //throw new ValidatorException(paramName + "不能为空");
            return;
        }

        try {
            boolean isJdkDate = DataType.isDate(paramValue.getClass()) || DataType.isTimestamp(paramValue.getClass());
            if(!isJdkDate) {
                ToolsKit.parseDate(paramValue.toString(), annonation.format());
            }
        } catch (Exception e) {
            try {
                ToolsKit.parseDate(paramValue.toString(), ConstEnums.DEFAULT_DATE_FORMAT_VALUE.getValue());
            } catch (Exception e1) {
                throw new ValidatorException(paramName +"["+paramValue+"]"+ annonation.message());
            }
        }

    }
}
