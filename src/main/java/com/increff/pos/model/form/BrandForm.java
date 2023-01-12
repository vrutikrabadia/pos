package com.increff.pos.model.form;




import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BrandForm {
    @NotNull(message = "Brand Cannot be empty")
    @NotBlank(message = "Brand Cannot be empty")
    private String brand;
    @NotNull(message = "Category Cannot be empty")
    @NotBlank(message = "Ctegory Cannot be empty")
    private String category;

}
