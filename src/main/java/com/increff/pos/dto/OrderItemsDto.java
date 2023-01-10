package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemsService;
// import com.increff.pos.service.OrderService;
import com.increff.pos.util.ConvertUtil;

@Component
public class OrderItemsDto {
    
    @Autowired
    private OrderItemsService service;

    // @Autowired
    // private OrderService oService;

    public void add(int orderId,List<OrderItemsForm> list) throws ApiException{
        List<OrderItemsPojo> list1 = new ArrayList<OrderItemsPojo>();

        // oService.getCheck(id);

        for(OrderItemsForm f: list){
            OrderItemsPojo p = ConvertUtil.convertOrderItemsFormToPojo(f);

            if(p.getQuantity() < 1){
                throw new ApiException("minimum quantity 1 required for order");
            }
            if(p.getSellingPrice() < 1){
                throw new ApiException("selling price should be non negative");
            }
            // p.setOrderId(id);
            list1.add(p);
        }

        service.add(orderId,list1);

    }

    public OrderItemsData getById(int id) throws ApiException{
        OrderItemsPojo p = service.selectById(id);

        return ConvertUtil.convertOrderItemsPojoToData(p);
    }

    public List<OrderItemsData> getByOrderId(int orderId) throws ApiException{
        List<OrderItemsData> list = new ArrayList<OrderItemsData>();
        List<OrderItemsPojo> list1 = service.selectByOrderId(orderId);

        for(OrderItemsPojo p: list1){
            list.add(ConvertUtil.convertOrderItemsPojoToData(p));
        }

        return list;

    }

    public void update(int id, OrderItemsForm f) throws ApiException{
        OrderItemsPojo p = ConvertUtil.convertOrderItemsFormToPojo(f);

        service.update(id, p);
    }
}
