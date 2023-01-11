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
        indexes ={@Index(name = "i_order_items_order_id", columnList = "order_id")})
@Getter
@Setter
public class OrderItemsPojo extends AbstractPojo implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "order_id", nullable = false)
    private Integer orderId;
    @Column(name = "product_id", nullable = false)
    private Integer productId;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "selling_price", nullable = false)
    private double sellingPrice;
}    
