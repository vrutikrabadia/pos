package com.increff.pos.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items",
        uniqueConstraints = {@UniqueConstraint(name="unique_order_id_product_id", columnNames = {"orderId", "productId"})})
@Getter
@Setter
public class OrderItemsPojo extends AbstractPojo implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(nullable=false)
    private Integer id;

    @Column(nullable=false)
    private Integer orderId;
    
    @Column(nullable=false)
    private Integer productId;
    
    @Column(nullable=false)
    private Integer quantity;
    
    @Column(nullable=false)
    private Double sellingPrice;
}    
