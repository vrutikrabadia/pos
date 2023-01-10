package com.increff.pos.model.data;

import com.increff.pos.model.form.OrderForm;

import lombok.Getter;
import lombok.Setter;

public class OrderData extends OrderForm{  
    @Getter @Setter private int id;
    @Getter @Setter private String dateTime;
}
