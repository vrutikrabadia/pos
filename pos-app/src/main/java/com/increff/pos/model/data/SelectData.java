package com.increff.pos.model.data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectData <T>{
    public SelectData(List<T> data, Integer draw, Integer recordsTotal, Integer recordsFiltered) {
        this.data = data;
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
    }

    private List<T> data;
    private Integer draw;
    private Integer recordsTotal;
    private Integer recordsFiltered; 
}
