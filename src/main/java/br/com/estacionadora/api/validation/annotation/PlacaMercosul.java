package br.com.estacionadora.api.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.estacionadora.api.validation.PlacaMercosulValidator;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = PlacaMercosulValidator.class)
public @interface PlacaMercosul {
	
    String message() default "{org.hibernate.validator.constraints.placa-mercosul.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
	
}
