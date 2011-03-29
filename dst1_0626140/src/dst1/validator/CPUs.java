package dst1.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CPUsValidator.class)
public @interface CPUs {
	int min() default 0;

	int max() default Integer.MAX_VALUE;

	String message() default "Die Anzahl der CPUs ist nicht erlaubt.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
