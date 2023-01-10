package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

public class OrderItemsForm {
    @Getter @Setter private String barCode;
    @Getter @Setter private int quantity;
    @Getter @Setter private double sellingPrice;
}
