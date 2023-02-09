package com.increff.pos.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {
    
    @Autowired
    private OrderDao dao;

    @Autowired
    private OrderItemsService itemsService;

     
    public OrderPojo add(List<OrderItemsPojo> itemsPojoList) throws ApiException{
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setEditable(true);
        
        dao.insert(orderPojo);

        itemsService.add(orderPojo.getId(), itemsPojoList);
        return orderPojo;
    }

      
    public OrderPojo get(Integer orderId) throws ApiException{
        return dao.selectByColumn("id", orderId);
    }

     
    public OrderPojo getCheck(Integer orderId) throws ApiException{
        OrderPojo orderPojo = get(orderId);
        if(Objects.isNull(orderPojo)){
            throw new ApiException("Order does not exists");
        }
        return orderPojo;
    }

     
    public List<OrderPojo> getAllPaginated(Integer offset, Integer pageSize){  
        return dao.selectAllPaginated(offset, pageSize);
   
    }

    public List<OrderPojo> getAll(){  
        return dao.selectAll();
   
    }

    public Integer getTotalEntries(){
        return dao.selectTotalEntries();
    }

    public List<OrderPojo> getInDateRange(ZonedDateTime startDate, ZonedDateTime endDate){
        return dao.selectInDateRange(startDate, endDate);
    }
}
