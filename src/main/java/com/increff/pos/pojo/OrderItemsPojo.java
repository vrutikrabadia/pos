package com.increff.pos.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
public class OrderItemsPojo {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter private int id;
    @Getter @Setter private int orderId;
    @Getter @Setter private int productId;
    @Getter @Setter private int quantity;
    @Getter @Setter private double sellingPrice;
}    
