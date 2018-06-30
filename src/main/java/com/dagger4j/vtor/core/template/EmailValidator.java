package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.PatternKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.vtor.annotation.constraints.Email;

import java.lang.annotation.Annotation;

/**
 * 邮箱验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class EmailValidator extends AbstractValidatorTemplate<Email> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Email.class;
    }

    @Override
    public void handle(Email annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }

        boolean isEmail = false;
        if(!".*".equals(annonation.regexp())) {
            isEmail = PatternKit.isMatch(annonation.regexp(), paramValue.toString());
        } else {
            isEmail = PatternKit.isEmail(paramValue.toString());
        }
        if(!isEmail) {
            throw new ValidatorException(paramName + annonation.message());
        }
    }
}
