package dst3.depinj.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
	ScopeType value() default ScopeType.PROTOTYPE;
}
