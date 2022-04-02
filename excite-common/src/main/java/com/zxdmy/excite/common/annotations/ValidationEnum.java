package com.zxdmy.excite.common.annotations;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.SneakyThrows;

import javax.validation.*;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/2 14:52
 */

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
@Repeatable(ValidationEnum.List.class)
public @interface ValidationEnum {
    //默认错误消息
    String message() default "必须为指定值";

    String[] strValues() default {};

    int[] intValues() default {};

    //使用指定枚举，1、使用属性命名code。2、枚举上使用QsmSpecifiedEnumValue
    Class<?> enumValue() default Class.class;

    //分组
    Class<?>[] groups() default {};

    //负载
    Class<? extends Payload>[] payload() default {};

    //指定多个时使用
    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValidationEnum[] value();
    }
}

/**
 * 枚举校验注解处理类
 */
class EnumValueValidator implements ConstraintValidator<ValidationEnum, Object> {

    private String[] strValues;
    private int[] intValues;
    private Class<?> cls;

    @Override
    public void initialize(ValidationEnum constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
        cls = constraintAnnotation.enumValue();
    }


    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }
        if (cls.isEnum()) {
            Object[] objects = cls.getEnumConstants();
            for (Object obj : objects) {
                //此处方法getCode需要根据自己项目枚举的命名而变化
                Method method = cls.getDeclaredMethod("getCode");
                String expectValue = String.valueOf(method.invoke(obj));
                if (expectValue.equals(String.valueOf(value))) {
                    return true;
                }
            }
        } else {
            if (value instanceof String) {
                for (String s : strValues) {
                    if (s.equals(value)) {
                        return true;
                    }
                }
            } else if (value instanceof Integer) {
                for (Integer s : intValues) {
                    if (s == value) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}