package com.increff.pdf.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemsForm {
    private String name;
    private String barcode;
    private Integer quantity;
    private Double sellingPrice;
}
