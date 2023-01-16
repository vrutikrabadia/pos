package com.increff.pos.util;

import java.lang.reflect.Field;

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
		    if(field.getType().getSimpleName() == "String"){
				
				field.setAccessible(true);
				try{
					field.set(form, toLowerCase((String)field.get(form)));
				} catch(IllegalAccessException e){
					throw new ApiException("Error normalising form");
				}
			}
		}
	}


}
