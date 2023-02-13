package com.increff.pos.model.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

    @NotBlank(message = "Barcode cannot be blank")
    @Size(min = 8, max = 8, message = "Barcode length must be 8 characters")
    private String barcode;
    
    @NotBlank(message = "Brand cannot be blank")
    @Size(max = 255, message = "Brand name length must be less than 255 characters")
    private String brand;
    
    @NotBlank(message = "Category cannot be blank")
    @Size(max = 255, message = "Category name length must be less than 255 characters")
    private String category;
    
    @NotBlank(message = "Product Name cannot be blank")
    @Size(max = 255, message = "Product Name length must be less than 255 characters")
    private String name;

    @NotNull(message = "MRP cannot be null")
    @Min(value = 0, message="MRP should be non negative")
    private Double mrp;
    
}
