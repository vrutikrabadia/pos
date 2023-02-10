package com.increff.pos.pojo;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pos_day_sales")
public class DaySalesPojo extends AbstractPojo {
    
    @Id

    private ZonedDateTime date;
    
    @Column(nullable=false)
    private Integer invoicedOrderCount;

    @Column(nullable=false)
    private Integer invoicedItemCount;
    
    @Column(nullable=false)
    private Double totalRevenue;
}
