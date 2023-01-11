package com.increff.pos.model.form;




import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandForm {
    @NotNull(message = "Brand Cannot be empty")
    @NotBlank(message = "Brand Cannot be empty")
    private String brand;
    @NotNull(message = "Category Cannot be empty")
    @NotBlank(message = "Ctegory Cannot be empty")
    private String category;

}
