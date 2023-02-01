package com.increff.pos.pojo;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PosDaySales extends AbstractPojo implements Serializable{
    
    @Id
    @Column(nullable=false)
    private ZonedDateTime date;
    
    @Column(nullable=false)
    private Integer invoicedOrderCount;
    
    @Column(nullable=false)
    private Integer invoicedItemsCount;
    
    @Column(nullable=false)
    private Double totalRevenue;
}
