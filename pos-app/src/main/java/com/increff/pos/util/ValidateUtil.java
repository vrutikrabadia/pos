package com.increff.pos.util;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.increff.pos.service.ApiException;


public class ValidateUtil {
    
    public static <T> void validateForms(T form){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> void validateList(List<T> formList) throws ApiException{
        JSONArray errorList = new JSONArray();

		for(T form: formList){


            try{
                validateForms(form);
            }
            catch(ConstraintViolationException e){
                JSONObject error = new JSONObject(new Gson().toJson(form));

                error.put("error", ExceptionUtil.getValidationMessage(e));
                errorList.put(error);
                
            }
		}

        if(errorList.length()>0)
            throw new ApiException(errorList.toString());
	} 
    
}


