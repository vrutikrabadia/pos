package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandForm {

    @NotBlank(message = "Brand cannot be blank")
    @Expose
    private String brand;

    
    @NotBlank(message = "Category cannot be blank")
    @Expose
    private String category;

}
