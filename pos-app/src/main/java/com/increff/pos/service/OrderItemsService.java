package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemsDao;
import com.increff.pos.pojo.OrderItemsPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemsService {
    
    @Autowired
    private OrderItemsDao dao;

    @Autowired 
    private InventoryService iService;

    @Autowired 
    DaySalesService sService;

     
    public void add(Integer orderId,List<OrderItemsPojo> orderItemsList) throws ApiException{
        
        Double revenue = 0.0;
        Integer totalQuantity = 0;
        for(OrderItemsPojo item: orderItemsList){
            revenue = revenue + (item.getQuantity()*item.getSellingPrice());
            totalQuantity += item.getQuantity();
            iService.reduceQuantity(item.getProductId(), item.getQuantity());
            item.setOrderId(orderId);
            dao.insert(item);
        }

    }

    public void add(OrderItemsPojo orderItem){
        dao.insert(orderItem);
    }

     
    public OrderItemsPojo getById(Integer itemId){
        return dao.selectByColumn("id",itemId);
    }

     
    public List<OrderItemsPojo> getByOrderId(Integer orderId){
        return dao.selectMultipleEntriesByColumn("orderId", orderId);
    }


    public <T> List<OrderItemsPojo> getInColumn(List<String> columns, List<List<T>> values){
        return dao.selectInColumns(columns, values);
    }
    
}
