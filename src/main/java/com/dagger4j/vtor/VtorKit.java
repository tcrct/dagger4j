//package com.dagger4j.vtor;
//
//import com.dagger4j.exception.MvcException;
//import org.hibernate.ValidatorHandler.HibernateValidator;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import java.util.Set;
//
///**
// * 使用hibernate的注解来进行验证
// * Created by laotang on 2018/6/29.
// */
//public class VtorKit {
//
//    private static Validator ValidatorHandler = null;
//
//    /**
//     * 功能描述: <br>
//     * 〈注解验证参数〉
//     *
//     * @param obj
//     * @see [相关类/方法](可选)
//     * @since [产品/模块版本](可选)
//     */
//    public static <T> void validate(T obj) {
//        if(null == ValidatorHandler) {
//            ValidatorHandler =Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
//        }
//        Set<ConstraintViolation<T>> constraintViolations = ValidatorHandler.validate(obj);
//        // 抛出检验异常
//        if (constraintViolations.size() > 0) {
//            throw new MvcException(String.format("参数校验失败:%s", constraintViolations.iterator().next().getMessage()));
//        }
//    }
//}
