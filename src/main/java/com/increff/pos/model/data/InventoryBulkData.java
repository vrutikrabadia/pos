package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryBulkData {
    private String barcode;
    private Integer quantity;
    private String error; 
}
