package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandForm {
    @NotBlank(message = "Brand cannot be blank")
    @Size(max = 255, message = "Brand name length must be less than 255 characters")
    private String brand;

    @NotBlank(message = "Category cannot be blank")
    @Size(max = 255, message = "Category name length must be less than 255 characters")
    private String category;

}
