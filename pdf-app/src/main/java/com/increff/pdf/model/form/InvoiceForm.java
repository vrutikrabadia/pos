package com.increff.pdf.model.form;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.increff.pdf.spring.CustomZonedDateTimeDeserializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceForm {
    
    @ApiModelProperty(required = true,example = "2020-07-14T21:56:00Z")
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime updated;
    private Integer id;
    private List<InvoiceItemsForm> itemsList;
    
}
