package com.increff.pos.model.form;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.increff.pos.spring.CustomZonedDateTimeDeserializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportForm {
    @ApiModelProperty(required = true,example = "2020-07-14T21:56:00Z")
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    @NotNull(message = "Start Date cannot be blank")
    private ZonedDateTime startDate;
    
    @ApiModelProperty(required = true,example = "2020-07-14T21:56:00Z")
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    @NotNull(message = "End Date cannot be blank")
    private ZonedDateTime endDate;

    @Size(max = 255, message = "Brand name length must be less than 255 characters")
    private String brand;

    @Size(max = 255, message = "Category name length must be less than 255 characters")
    private String category;
}
