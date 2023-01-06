package com.increff.pos.model.data;

import com.increff.pos.model.form.ProductForm;

import lombok.Getter;
import lombok.Setter;

public class ProductData extends ProductForm{
    @Getter @Setter private int id;
    @Getter @Setter private String barCode;
    @Getter @Setter private int brandCat;
    
}
