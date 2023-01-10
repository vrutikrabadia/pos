package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemsDao;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;

@Service
public class OrderItemsService {
    
    @Autowired
    private OrderItemsDao dao;

    @Autowired 
    private InventoryService iService;

    @Autowired
    private OrderService oService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(int orderId,List<OrderItemsPojo> list) throws ApiException{
        
        for(OrderItemsPojo p: list){
            iService.reduceQuantity(p.getProductId(), p.getQuantity());
            p.setOrderId(orderId);
            dao.insert(p);
        }

    }

    @Transactional
    public OrderItemsPojo selectById(int id){
        return dao.selectById(id);
    }

    @Transactional
    public List<OrderItemsPojo> selectByOrderId(int orderId){
        return dao.selectByOrderId(orderId);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, OrderItemsPojo p) throws ApiException{
        
        
        OrderItemsPojo p1 = selectById(id);

        if(p1.getProductId() != p.getProductId()){
            throw new ApiException("Mismatch in product barcode provided");
        }

        OrderPojo op = oService.get(p1.getOrderId());

        if(!op.isEditable()){
            throw new ApiException("Order is no longer editable");
        }

        if(p1.getQuantity() > p.getQuantity()){
            //increase quantity in inventory
            iService.increaseQuantity(p1.getProductId(), p1.getQuantity()-p.getQuantity());
        }
        else if(p1.getQuantity() < p.getQuantity()){
            //decrease quantity in inventory
            iService.reduceQuantity(p1.getProductId(), p.getQuantity()-p1.getQuantity());
        }

        p1.setQuantity(p.getQuantity());
        p1.setSellingPrice(p.getSellingPrice());
        dao.update(p1);
    }
    
}
