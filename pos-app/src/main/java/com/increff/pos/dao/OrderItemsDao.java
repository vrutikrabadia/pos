package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemsDao extends AbstractDao<OrderItemPojo>{

    public OrderItemsDao() {
        super(OrderItemPojo.class);
    }
    
}
