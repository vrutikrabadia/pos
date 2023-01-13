package com.increff.pos.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;



public class ExceptionUtil {
    
    

    public static String getValidationMessage(ConstraintViolationException ex) {
		List<String> details = ex.getConstraintViolations()
				.parallelStream()
				.map(e -> e.getMessage())
				.collect(Collectors.toList());

		return details.toString();
	}
 
}
