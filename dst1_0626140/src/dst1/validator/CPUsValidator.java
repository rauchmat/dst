package dst1.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPUsValidator implements ConstraintValidator<CPUs, Integer> {
	private CPUs annotation;

	@Override
	public void initialize(CPUs annotation) {
		this.annotation = annotation;
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return value >= annotation.min() && value <= annotation.max();
	}
}
