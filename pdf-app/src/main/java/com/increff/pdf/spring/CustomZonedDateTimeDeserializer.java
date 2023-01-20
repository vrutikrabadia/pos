package com.increff.pdf.spring;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomZonedDateTimeDeserializer extends JsonDeserializer {

    final DateTimeFormatter formatter  = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        String date = jsonParser.getText();

        

        // TemporalAccessor accessor=
        // DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(date);
        return ZonedDateTime.parse(date, formatter);

    }
}
