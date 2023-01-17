package com.increff.pos.model.data;

import com.increff.pos.model.form.OrderItemsForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsData extends OrderItemsForm{
    private Integer id;
    private Integer orderId;
}
