package com.increff.pos.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.increff.pos.model.data.MessageData;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.ExceptionUtil;

@RestControllerAdvice
public class AppRestControllerAdvice {

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(ApiException e) {
		MessageData data = new MessageData();
		data.setMessage(e.getMessage());
		return data;
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(Throwable e) {
		MessageData data = new MessageData();
		data.setMessage("An unknown error has occurred - " + e.getMessage());
		return data;
	}

	// @ExceptionHandler(MethodArgumentNotValidException.class)
	// @ResponseStatus(HttpStatus.BAD_REQUEST)
	// public MessageData handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
	// 	BindingResult err = error.getBindingResult();
	// 	MessageData data = new MessageData();

	// 	data.setMessage(ExceptionUtil.getValidationMessage(err));
	// 	return data;
	// }

	// @ExceptionHandler(ConstraintViolationException.class)
	// @ResponseStatus(HttpStatus.BAD_REQUEST)
	// public final MessageData handleConstraintViolation(
	// 		ConstraintViolationException ex,
	// 		WebRequest request) {
	// 	List<String> details = ex.getConstraintViolations()
	// 			.parallelStream()
	// 			.map(e -> e.getMessage())
	// 			.collect(Collectors.toList());

	// 	MessageData data = new MessageData();

	// 	data.setMessage("details");
	// 	return data;
	// }
}