package com.increff.pos.util;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


public class ExceptionUtil {
    
    // public static String convertBindingResultToApiException(BindingResult error){
    //     return new ApiException(getValidationMessage(error));
    // }

    public static String getValidationMessage(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        
        String apiError = "";
        
        for(ObjectError e: allErrors){
            apiError += getValidationMessage(e);
        }

        return apiError;
    }
 
    private static String getValidationMessage(ObjectError error) {
        if (error instanceof FieldError) {
            FieldError fieldError = (FieldError) error;
            String className = fieldError.getObjectName();
            String property = fieldError.getField();
            Object invalidValue = fieldError.getRejectedValue();
            String message = fieldError.getDefaultMessage();
            return String.format("%s.%s %s, but it was %s", className, property, message, invalidValue);
        }
        return String.format("%s: %s", error.getObjectName(), error.getDefaultMessage());
    }
}
