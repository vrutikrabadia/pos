package com.increff.pdf.model.data;

import javax.validation.constraints.NotBlank;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandReportData {

    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    
    @NotBlank(message = "Category cannot be blank")
    private String category;

    private Integer id;

}
