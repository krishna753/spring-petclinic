package org.springframework.samples.petclinic.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Calendar;

public class PresentOrFutureValidator implements ConstraintValidator<PresentOrFuture, LocalDate> {

	@Override
	public final void initialize(final PresentOrFuture annotation) {
	}

	public final boolean isValid(final LocalDate value, final ConstraintValidatorContext context) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return LocalDate.now().isBefore(value) || LocalDate.now().isEqual(value);
	}
}
