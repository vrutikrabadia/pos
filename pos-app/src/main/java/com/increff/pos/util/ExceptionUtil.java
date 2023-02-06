package com.increff.pos.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.increff.pos.service.ApiException;

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
			JSONObject error = new JSONObject(new Gson().toJson(e));
			error.put("error", message);
			errorList.put(error);
		});

		formSet.forEach(e -> {
			JSONObject error = new JSONObject(new Gson().toJson(e));
			error.put("error", "");
			errorList.put(error);
		});

		throw new ApiException(errorList.toString());
	}

	public static <T, R> void generateBulkAddExceptionPojo(String message, List<T> pojoList, Set<T> repeatSet, Class<R> clazz)
			throws ApiException {
		Set<T> formSet = pojoList.stream().filter(e->!repeatSet.contains(e)).collect(Collectors.toSet());
				
		JSONArray errorList = new JSONArray();
		formSet.forEach(e->{
			R repeated = ConvertUtil.objectMapper(e, clazz);
			JSONObject error = new JSONObject(new Gson().toJson(repeated));
			error.put("error", "");
			errorList.put(error);
		});
		repeatSet.forEach(e -> {
			R repeated = ConvertUtil.objectMapper(e, clazz);
			JSONObject error = new JSONObject(new Gson().toJson(repeated));
			error.put("error", "DUPLICATE: already exists in db");
			errorList.put(error);
		});
		throw new ApiException(errorList.toString());
	}



}
