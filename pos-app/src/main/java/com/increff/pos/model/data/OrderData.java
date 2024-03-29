package com.increff.pos.model.data;


import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.spring.CustomZonedDateTimeSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData extends OrderForm{  
    private Integer id;
    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    private ZonedDateTime updated;
}
