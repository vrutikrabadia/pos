package com.increff.pos.model.data;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.increff.pos.spring.CustomZonedDateTimeSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceData {
    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    private ZonedDateTime updated;
    private Integer id;
    private List<InvoiceItemsData> itemsList;
    
}
