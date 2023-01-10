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
        return ConvertUtil.convertOrderPojoToData(service.add(p));
    }

    public OrderData get(int id) throws ApiException{
        return ConvertUtil.convertOrderPojoToData(service.get(id));
    }

    public List<OrderData> getAll(){
        List<OrderData> list = new ArrayList<OrderData>();
        List<OrderPojo> list1 = service.getAll();

        for(OrderPojo p : list1){
            list.add(ConvertUtil.convertOrderPojoToData(p));
        }

        return list;
    }

    public OrderData update(int id, OrderForm f) throws ApiException{
        return ConvertUtil.convertOrderPojoToData(service.update(id, null));
    }

    public void finaliseOrder(int id) throws ApiException{
        service.finaliseOrder(id);
    }

}
