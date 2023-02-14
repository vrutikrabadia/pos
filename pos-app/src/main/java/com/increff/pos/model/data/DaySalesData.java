package com.increff.pos.model.data;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.increff.pos.spring.CustomZonedDateTimeSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DaySalesData{
    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    private ZonedDateTime date;
    private Integer invoicedOrderCount;
    private Integer invoicedItemCount;
    private Double totalRevenue;  
}
