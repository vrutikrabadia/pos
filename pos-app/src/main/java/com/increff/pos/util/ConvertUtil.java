package com.increff.pos.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Used to map objects of one class to objects of another class
 */
public class ConvertUtil {
    
	/**
	 * This method is used to map a object from source class to destination class
	 * Maps all the values from source to destination that have same variable name and dataType.
	 * @param <T> T source object type
	 * @param <R> R destination object type
	 * @param object
	 * @param destinationClassType
	 * @return R
	 */
	public static <T,R> R objectMapper(T object, Class<R> destinationClassType){
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.registerModule(new JavaTimeModule());

    	R newObject = mapper.convertValue(object, destinationClassType);
        return newObject;
    }

}
