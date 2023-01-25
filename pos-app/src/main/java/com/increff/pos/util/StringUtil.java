package com.increff.pos.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import com.increff.pos.service.ApiException;


/**	
 * Contains methods for string normalisation.
 */
public class StringUtil {

	
	/** 
	 * Method to convert string to lower case and trim extra space before and after the string.
	 * @param string
	 * @return String
	 */
	public static String toLowerCase(String string) {
		return string == null ? null : string.trim().toLowerCase();
	}

	
	/** 
	 * Method to normalise all the values of the fiels with String datatype in the object.
	 * @param <T> class of the object
	 * @param object
	 * @param classType
	 * @throws ApiException
	 */
	public static <T> void normalise(T object, Class<T> classType) throws ApiException {
		Field[] fields = classType.getDeclaredFields();

		for(Field field: fields) {
			
		    if(field.getType().getSimpleName().equals("String")){
				field.setAccessible(true);
				try{
					if(Objects.nonNull(field.get(object))){
						field.set(object, toLowerCase(field.get(object).toString()));
					}
				} catch(IllegalAccessException e){
					throw new ApiException("Error normalising form");
				}
			}
		}
	}


	
	/** 
	 * Method to normalise all the values of the fiels with String datatype in the List of objects.
	 * @param <T> class of the object
	 * @param objectList
	 * @param classType
	 * @throws ApiException
	 */
	public static <T> void normaliseList(List<T> objectList, Class<T> classType) throws ApiException {
		for(T form: objectList){
			normalise(form, classType);
		}
	} 

}
