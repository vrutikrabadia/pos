package com.increff.pos.model.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsForm {

    @NotBlank(message = "Barcode cannot be blank")
    @Size(min = 8, max = 8, message = "Barcode length must be 8 characters")
    private String barcode;

    //TODO: add >1 constraint
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message="Quantity should be non negative")
    private Integer quantity;

    @NotNull(message = "Selling price cannot be null")
    @Min(value = 0, message="Selling price should be non negative")
    private Double sellingPrice;
}
