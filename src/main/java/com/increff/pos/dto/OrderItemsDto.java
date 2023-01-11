package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;

@Component
public class OrderItemsDto {
    
    @Autowired
    private OrderItemsService service;

    @Autowired
    private ProductService pService;

    public OrderData add(Integer orderId,List<OrderItemsForm> list) throws ApiException{
        List<OrderItemsPojo> list1 = new ArrayList<OrderItemsPojo>();

        for(OrderItemsForm f: list){
            OrderItemsPojo p = ConvertUtil.objectMapper(f, OrderItemsPojo.class);

            ProductPojo product = pService.get(f.getBarcode());

            p.setProductId(product.getId());

            if(p.getQuantity() < 1){
                throw new ApiException("minimum quantity 1 required for order");
            }
            if(p.getSellingPrice() < 1){
                throw new ApiException("selling price should be non negative");
            }
            list1.add(p);
        }

        return ConvertUtil.objectMapper(service.add(list1), OrderData.class);

    }

    public OrderItemsData getById(Integer id) throws ApiException{
        OrderItemsPojo p = service.selectById(id);
        ProductPojo product = pService.get(p.getId());
        OrderItemsData data = ConvertUtil.objectMapper(p, OrderItemsData.class);
        data.setBarcode(product.getBarcode());
        return data;
    }

    public List<OrderItemsData> getByOrderId(Integer orderId) throws ApiException{
        List<OrderItemsData> list = new ArrayList<OrderItemsData>();
        List<OrderItemsPojo> list1 = service.selectByOrderId(orderId);

        for(OrderItemsPojo p: list1){

            ProductPojo product = pService.get(p.getId());
            OrderItemsData data = ConvertUtil.objectMapper(p, OrderItemsData.class);
            data.setBarcode(product.getBarcode());

            list.add(data);
        }

        return list;

    }

    public void update(Integer id, OrderItemsForm f) throws ApiException{
        OrderItemsPojo p = ConvertUtil.objectMapper(f, OrderItemsPojo.class);
        ProductPojo product = pService.get(f.getBarcode());
        p.setProductId(product.getId());

        service.update(id, p);
    }
}
