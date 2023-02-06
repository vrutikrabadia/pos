package com.increff.pdf.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemsData {
    private String name;
    private String barcode;
    private Integer quantity;
    private Double sellingPrice;
}
