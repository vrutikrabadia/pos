package com.increff.pos.service;

import java.time.ZonedDateTime;
import java.util.List;

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
    private OrderItemsService iService;

     
    public OrderPojo add(List<OrderItemsPojo> list) throws ApiException{
        OrderPojo p = new OrderPojo();
        p.setEditable(true);
        
        dao.insert(p);

        iService.add(p.getId(), list);

        return p;
    }

      
    public OrderPojo get(Integer id) throws ApiException{
        return getCheck(id);
    }

     
    public OrderPojo getCheck(Integer id) throws ApiException{
        OrderPojo p =dao.selectByColumn("id", id);
        if(p == null){
            throw new ApiException("Order does not exists");
        }
        return p;
    }

     
    public List<OrderPojo> getAllPaginated(Integer offset, Integer pageSize){  
        return dao.selectAllPaginated(offset, pageSize);
   
    }

    public List<OrderPojo> getAll(){  
        return dao.selectAll();
   
    }

    public Integer getTotalEntries(){
        return dao.getTotalEntries();
    }

    public List<OrderPojo> getInDateRange(ZonedDateTime startDate, ZonedDateTime endDate){
        return dao.selectInDateRange(startDate, endDate);
    }
}
