package com.increff.pos.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import com.increff.pos.service.ApiException;

public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	public static <T> void normalise(T form, Class<T> type) throws ApiException {
		Field[] fields = type.getDeclaredFields();

		for(Field field: fields) {
			
		    if(field.getType().getSimpleName().equals("String")){
				field.setAccessible(true);
				try{
					if(Objects.nonNull(field.get(form))){
						field.set(form, toLowerCase(field.get(form).toString()));
					}
				} catch(IllegalAccessException e){
					throw new ApiException("Error normalising form");
				}
			}
		}
	}


	public static <T> void normaliseList(List<T> formList, Class<T> type) throws ApiException {
		for(T form: formList){
			normalise(form, type);
		}
	} 

}
