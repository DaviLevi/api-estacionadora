package br.com.estacionadora.api.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import br.com.estacionadora.api.validation.annotation.PlacaMercosul;

@Component
public class PlacaMercosulValidator implements ConstraintValidator<PlacaMercosul,String> {
	
	private final Pattern placaPattern = Pattern.compile("^[A-Z]{3}[0-9][0-9A-Z][0-9]{2}$");
	
    public PlacaMercosulValidator() { }
 
    public void initialize(PlacaMercosul constraint) { }
    
    @Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
    	if (value == null) return true;    	
    	return placaPattern.matcher(value).matches();
	}
    
 
 
}