package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemsDao;
import com.increff.pos.pojo.OrderItemPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemsService {
    
    @Autowired
    private OrderItemsDao dao;

    @Autowired 
    private InventoryService iService;

     
    public void add(Integer orderId,List<OrderItemPojo> orderItemsList) throws ApiException{
        
        for(OrderItemPojo item: orderItemsList){
            item.setOrderId(orderId);
            dao.insert(item);
        }

    }

    public void add(OrderItemPojo orderItem){
        dao.insert(orderItem);
    }

     
    public OrderItemPojo getById(Integer itemId){
        return dao.selectByColumn("id",itemId);
    }

     
    public List<OrderItemPojo> getByOrderId(Integer orderId){
        return dao.selectMultipleEntriesByColumn("orderId", orderId);
    }


    public <T> List<OrderItemPojo> getInColumn(List<String> columns, List<List<T>> values){
        return dao.selectInColumns(columns, values);
    }
    
}
