package com.increff.pos.spring;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomZonedDateTimeDeserializer extends JsonDeserializer {

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        String date = jsonParser.getText();
        
        return LocalDate.parse(date, dtf).atStartOfDay(ZoneOffset.UTC);
        

    }
}
