package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandForm {
    @NotBlank(message = "Brand cannot be blank")
    private String brand;
    @NotBlank(message = "Category cannot be blank")
    private String category;

}
