package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandForm {
    // TODO: check length of Strings in forms. 0-255
    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @NotBlank(message = "Category cannot be blank")
    private String category;

}
