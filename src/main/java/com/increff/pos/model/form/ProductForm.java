package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

public class ProductForm {
    @Getter @Setter private String barCode;
    @Getter @Setter private String brand;
    @Getter @Setter private String category;
    @Getter @Setter private String name;
    @Getter @Setter private double mrp;
    
}
