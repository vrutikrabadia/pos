package com.increff.pos.model.data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandSelectData {
    private List<BrandData> data;
    private Integer draw;
    private Integer recordsTotal;
    private Integer recordsFiltered;    
}
