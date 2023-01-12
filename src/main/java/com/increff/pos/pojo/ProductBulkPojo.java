package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductBulkPojo {
    private String barcode;
    private Integer brandCat;
    private String name;
    private Double mrp;
    private String error;
}
