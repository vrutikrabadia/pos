package com.increff.pdf.model.data;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceData {
    
    private ZonedDateTime updated;
    private Integer id;
    private List<InvoiceItemsData> itemsList;
    
}
