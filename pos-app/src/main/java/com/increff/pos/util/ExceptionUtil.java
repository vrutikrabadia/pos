package com.increff.pos.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;


/**
 * Used to handle the exceptions being thrown in the applications.
 * The methods mainly extracts the exception message.
 */
public class ExceptionUtil {
    
    
	/** 
	 * This method extracts the constraint violation messages from the ConstraintViolationException and returns a string
	 * @param constraintViolationException 
	 * @return String
	 */
	public static String getValidationMessage(ConstraintViolationException constraintViolationException) {
		List<String> details = constraintViolationException.getConstraintViolations()
				.parallelStream()
				.map(e -> e.getMessage())
				.collect(Collectors.toList());

		return details.toString();
	}
 
}
