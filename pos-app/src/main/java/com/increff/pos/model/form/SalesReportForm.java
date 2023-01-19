package com.increff.pos.model.form;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.increff.pos.spring.CustomZonedDateTimeDeserializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportForm {
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    @NotNull(message = "startDate cannot be blank")
    ZonedDateTime startDate;
    
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    @NotNull(message = "endDate cannot be blank")
    ZonedDateTime endDate;

    String brand;
    String category;
}
