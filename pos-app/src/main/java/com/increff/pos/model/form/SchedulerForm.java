package com.increff.pos.model.form;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulerForm {

    @NotNull(message = "Items count cannot be null")
    @Min(value = 1, message = "Item count should be possitive")
    private Integer itemsCount;

    @NotNull(message = "Revenue cannot be null")
    @Min(value = 0, message = "Revenue should be an non negative quantity")
    private Double totalRevenue;    
}
