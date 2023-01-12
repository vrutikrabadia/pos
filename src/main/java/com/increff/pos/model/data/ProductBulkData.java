package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductBulkData {
    private String barcode;
    private String brand;
    private String category;
    private String name;
    private double mrp;
    private String error;    
}
