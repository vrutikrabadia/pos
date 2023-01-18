package com.increff.pos.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// @Component
public class ConvertUtil<T> {
    public static <T,R> R objectMapper(T object, Class<R> destinClass){
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.registerModule(new JavaTimeModule());

    	R newObject = mapper.convertValue(object, destinClass);
        return newObject;
    }

}
