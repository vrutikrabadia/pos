package com.increff.pos.spring;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomZonedDateTimeSerializer extends JsonSerializer {

  public static final DateTimeFormatter FORMATTER = 
  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.FRANCE);
  
  @Override
  public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      gen.writeString(((ZonedDateTime)value).format(FORMATTER));
  }    
}

