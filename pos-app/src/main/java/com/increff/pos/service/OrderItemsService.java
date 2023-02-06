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

     
    public void add(Integer orderId,List<OrderItemsPojo> list) throws ApiException{
        
        Double revenue = 0.0;
        Integer totalQuantity = 0;
        for(OrderItemsPojo p: list){
            revenue = revenue + (p.getQuantity()*p.getSellingPrice());
            totalQuantity += p.getQuantity();
            iService.reduceQuantity(p.getProductId(), p.getQuantity());
            p.setOrderId(orderId);
            dao.insert(p);
        }

    }

    public void add(OrderItemsPojo item){
        dao.insert(item);
    }

     
    public OrderItemsPojo selectById(Integer id){
        return dao.selectByColumn("id",id);
    }

     
    public List<OrderItemsPojo> selectByOrderId(Integer orderId){
        return dao.selectMultiple("orderId", orderId);
    }


    public <T> List<OrderItemsPojo> getInColumn(List<String> columns, List<List<T>> values){
        return dao.selectByColumnUsingIn(columns, values);
    }
    
}
