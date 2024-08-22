package com.project.Electronic_Store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {

    //error message
    String message() default "Invalid image name!!";
    //represent group of constraints
    Class<?>[] groups() default { };
    //additional information about annotations
    Class<? extends Payload>[] payload() default { };
}
