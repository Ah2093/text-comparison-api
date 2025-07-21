package eg.com.tokencomparison.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = TextFileValidator.class)
@Target({ANNOTATION_TYPE, FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTextFile {

    String message() default "Invalid file: must be a .txt file and not larger than 10MB";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}