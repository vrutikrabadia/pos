package com.increff.pos.model.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryForm {

    @NotBlank(message = "Barcode cannot be blank")
    @Size(min = 8, max = 8, message = "Barcode length must be 8 characters")
    private String barcode;
    
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message="Quantity should be non negative")
    private Integer quantity;
}
