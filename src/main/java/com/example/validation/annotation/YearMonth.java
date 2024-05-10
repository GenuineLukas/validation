package com.example.validation.annotation;

import com.example.validation.validator.YearMonthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = YearMonthValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//여러 어노테이션을 종합 할 수 있다
@NotBlank
public @interface YearMonth {
    String message() default "year month 양식에 맞지 않습니다. ex)20230213";
    String pattern() default "yyyyMMdd";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
