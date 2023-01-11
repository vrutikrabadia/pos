package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.ConvertUtil;

@Component
public class OrderDto {
    
    @Autowired
    private OrderService service;

    public OrderData add(OrderForm f){
        OrderPojo p = new OrderPojo();
        return ConvertUtil.objectMapper(service.add(p), OrderData.class);
    }

    public OrderData get(Integer id) throws ApiException{
        return ConvertUtil.objectMapper(service.get(id), OrderData.class);
    }

    public List<OrderData> getAll(){
        List<OrderData> list = new ArrayList<OrderData>();
        List<OrderPojo> list1 = service.getAll();

        for(OrderPojo p : list1){
            list.add(ConvertUtil.objectMapper(p, OrderData.class));
        }

        return list;
    }

    public OrderData update(Integer id, OrderForm f) throws ApiException{
        return ConvertUtil.objectMapper(service.update(id, null), OrderData.class);
    }

    public void finaliseOrder(Integer id) throws ApiException{
        service.finaliseOrder(id);
    }

}
