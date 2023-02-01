package com.increff.pos.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items",
        indexes ={@Index(name = "i_order_items_order_id", columnList = "orderId")})
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
