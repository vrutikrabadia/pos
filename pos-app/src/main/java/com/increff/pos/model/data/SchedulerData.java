package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulerData{
    private String date;
    private Integer orderCount;
    private Integer itemsCount;
    private Double totalRevenue;  
}
