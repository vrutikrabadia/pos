package com.increff.pos.spring;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomZonedDateTimeSerializer extends JsonSerializer {

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
  
  @Override
  public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      gen.writeString(((ZonedDateTime)value).format(FORMATTER));
  }    
}

