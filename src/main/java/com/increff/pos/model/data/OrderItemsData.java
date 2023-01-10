package com.increff.pos.model.data;

import com.increff.pos.model.form.OrderItemsForm;

import lombok.Getter;
import lombok.Setter;

public class OrderItemsData extends OrderItemsForm{
    @Getter @Setter private int id;
    @Getter @Setter private int orderId;
}
