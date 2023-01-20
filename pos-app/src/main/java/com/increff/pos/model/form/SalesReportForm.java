package com.increff.pos.model.form;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.increff.pos.spring.CustomZonedDateTimeDeserializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportForm {
    @ApiModelProperty(required = true,example = "2016-01-01")
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    @NotNull(message = "startDate cannot be blank")
    ZonedDateTime startDate;
    
    @ApiModelProperty(required = true,example = "2016-01-01")
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    @NotNull(message = "endDate cannot be blank")
    ZonedDateTime endDate;

    String brand;
    String category;
}
