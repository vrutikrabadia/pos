package com.increff.pos.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * Used to handle the exceptions being thrown in the applications.
 * The methods mainly extracts the exception message.
 */
public class ExceptionUtil {

	/**
	 * This method extracts the constraint violation messages from the
	 * ConstraintViolationException and returns a string
	 * 
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

	public static <T> void generateBulkAddExceptionList(String message, List<T> formList, Set<T> repeatSet)
			throws ApiException {
		Set<T> formSet = formList.stream().filter(e -> !repeatSet.contains(e)).collect(Collectors.toSet());

		JSONArray errorList = new JSONArray();
		repeatSet.forEach(e -> {
			errorList.put(generateJSONErrorObject(message, e));
		});

		formSet.forEach(e -> {
			errorList.put(generateJSONErrorObject(message, e));
		});

		throw new ApiException(errorList.toString());
	}

	public static <T, R> void generateBulkAddExceptionPojo(String message, List<T> pojoList, Set<T> repeatSet, Class<R> clazz)
			throws ApiException {
		Set<T> formSet = pojoList.stream().filter(e->!repeatSet.contains(e)).collect(Collectors.toSet());
				
		JSONArray errorList = new JSONArray();
		formSet.forEach(e->{
			R repeated = ConvertUtil.objectMapper(e, clazz);
			errorList.put(generateJSONErrorObject(message, repeated));
		});
		repeatSet.forEach(e -> {
			R repeated = ConvertUtil.objectMapper(e, clazz);
			errorList.put(generateJSONErrorObject(message, repeated));
		});
		throw new ApiException(errorList.toString());
	}

	public static <T> JSONObject generateJSONErrorObject(String message, T object){
		JSONObject error = new JSONObject(new Gson().toJson(object));
		error.put("error", message);
		return error;
	}



}
