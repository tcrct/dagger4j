package com.dagger4j.vtor.core.template;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.utils.DataType;
import com.dagger4j.vtor.annotation.Range;

import java.lang.annotation.Annotation;

/**
 * 取值范围验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class RangeValidator extends AbstractValidatorTemplate<Range> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Range.class;
    }

    @Override
    public void handle(Range annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }
        Double value =Double.parseDouble(paramValue.toString());

        double max = annonation.max();
        double min = annonation.min();
        double[] annonValues = annonation.value();
        if(annonValues[0] !=-1d && annonValues[1] != -1d) {
            min = annonValues[0];
            max = annonValues[1];
        }
        if( value > max || value< min ) {
            String maxString = max+"";
            String minString = min+"";
            if(DataType.isInteger(parameterType) || DataType.isIntegerObject(parameterType)) {
                maxString = Double.valueOf(max).intValue()+"";
                minString = Double.valueOf(min).intValue()+"";
            }
            String message = paramName+"["+paramValue+"]"+annonation.message().replace("${min}", minString).replace("${max}", maxString);
            throw new ValidatorException(message);
        }
    }
}
