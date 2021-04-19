package org.springframework.samples.petclinic.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekDayValidator
	implements ConstraintValidator<WeekDay, LocalDate> {

	public final void initialize(final PresentOrFuture annotation) {}

	public final boolean isValid(final LocalDate value,
								 final ConstraintValidatorContext context) {
		return ! (value.getDayOfWeek().equals(DayOfWeek.SATURDAY)
			|| value.getDayOfWeek().equals(DayOfWeek.SUNDAY));
	}
}
