package dst3.depinj.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
	boolean required() default false;

	Class<?> specificType() default Object.class;
}
