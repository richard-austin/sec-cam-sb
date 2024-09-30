package com.server.validators.custom

import jakarta.validation.Constraint
import org.springframework.messaging.handler.annotation.Payload

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Documented
@Constraint(validatedBy = BooleanConstraint.class)
@Target([ElementType.METHOD, ElementType.FIELD])
@Retention(RetentionPolicy.RUNTIME)
@interface  IsBoolean {
    String message() default "Boolean value should not be null";
    Class<?>[] groups() default []
    Class<? extends Payload>[] payload() default []

}
