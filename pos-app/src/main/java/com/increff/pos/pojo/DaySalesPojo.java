package com.increff.pos.pojo;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pos_day_sales")
@Getter
@Setter
public class DaySalesPojo extends AbstractPojo implements Serializable{
    
    @Id
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;
    @Column(name = "invoiced_orders_count", nullable = false)
    private Integer orderCount;
    @Column(name = "invoiced_items_count", nullable = false)
    private Integer itemsCount;
    @Column(name = "total_revenue", nullable = false)
    private Double totalRevenue;
}
