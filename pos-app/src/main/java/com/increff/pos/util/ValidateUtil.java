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

/**
 * Contains method that validates the input forms recieved in the request.
 */
public class ValidateUtil {

    /**
     * This method validates the form.
     * The rules are specified in the form class using validation annotations.
     * 
     * @param <T>  T class of the form object
     * @param form
     * @throws ConstraintViolationException
     */
    public static <T> void validateForms(T form) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    /**
     * This method validates a list form.
     * The rules are specified in the form class using validation annotations.
     * 
     * @param <T>      T class of the form
     * @param formList
     * @throws ApiException
     */
    public static <T> void validateList(List<T> formList) throws ApiException {
        JSONArray errorList = new JSONArray();

        Integer countErrors = 0;
        for (T form : formList) {

            try {
                validateForms(form);
            } catch (ConstraintViolationException e) {
                countErrors++;
                JSONObject error = new JSONObject(new Gson().toJson(form));

                error.put("error", ExceptionUtil.getValidationMessage(e));
                errorList.put(error);
                continue;
            }
            JSONObject error = new JSONObject(new Gson().toJson(form));

            error.put("error", "");
            errorList.put(error);
        }

        if (countErrors > 0)
            throw new ApiException(errorList.toString());
    }

    public static void validateEmail(String email) throws ApiException {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        String errorMessage = "Invalid Email format.";
        if (!email.matches(emailRegex)) {
            throw new ApiException(errorMessage);
        }
    }

    public static void validatePassword(String password) throws ApiException {
        if (password.length() < 8) {
            throw new ApiException("Password must be of atleast 8 characters");
        }
    }

}
