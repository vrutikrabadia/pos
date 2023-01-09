package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

public class InventoryForm {
    @Getter @Setter private String barCode;
    @Getter @Setter private int quantity;
}
