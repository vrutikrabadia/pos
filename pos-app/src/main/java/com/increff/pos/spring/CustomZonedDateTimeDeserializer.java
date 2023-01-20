package com.increff.pos.spring;

import java.io.IOException;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomZonedDateTimeDeserializer extends JsonDeserializer {

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        String date = jsonParser.getText();
        
        return ZonedDateTime.parse(date);
        


    }
}
