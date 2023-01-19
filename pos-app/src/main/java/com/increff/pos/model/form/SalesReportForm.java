package com.increff.pos.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportForm {
    @NotBlank(message = "startDate cannot be blank")
    String startDate;
    
    @NotBlank(message = "endDate cannot be blank")
    String endDate;

    String brand;
    String category;
}
