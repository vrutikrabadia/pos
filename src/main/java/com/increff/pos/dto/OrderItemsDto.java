package com.increff.pos.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class OrderItemsDto {

    @Autowired
    private OrderItemsService service;

    @Autowired
    private ProductService pService;

    @Autowired
    private OrderService oService;

    public OrderData add(List<OrderItemsForm> list) throws ApiException {
        List<OrderItemsPojo> list1 = new ArrayList<OrderItemsPojo>();

        for (OrderItemsForm f : list) {
            OrderItemsPojo p = ConvertUtil.objectMapper(f, OrderItemsPojo.class);

            ProductPojo product = pService.get(f.getBarcode());

            p.setProductId(product.getId());

            ValidateUtil.validateOrderItem(p);
            list1.add(p);
        }

        OrderPojo order = service.add(list1);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(order.getUpdated());
        OrderData data = ConvertUtil.objectMapper(order, OrderData.class);
        data.setUpdated(strDate);

        return data;

    }

    public void addToExisitingOrder(Integer orderId, OrderItemsForm form) throws ApiException {
        OrderItemsPojo item = ConvertUtil.objectMapper(form, OrderItemsPojo.class);

        oService.getCheck(orderId);
        
        ProductPojo product = pService.get(form.getBarcode());
        item.setProductId(product.getId());

        item.setOrderId(orderId);

        service.add(item);

    }

    public OrderItemsData getById(Integer id) throws ApiException {
        OrderItemsPojo p = service.selectById(id);
        ProductPojo product = pService.get(p.getId());
        OrderItemsData data = ConvertUtil.objectMapper(p, OrderItemsData.class);
        data.setBarcode(product.getBarcode());
        return data;
    }

    public List<OrderItemsData> getByOrderId(Integer orderId) throws ApiException {
        List<OrderItemsData> list = new ArrayList<OrderItemsData>();
        List<OrderItemsPojo> list1 = service.selectByOrderId(orderId);

        for (OrderItemsPojo p : list1) {

            ProductPojo product = pService.get(p.getProductId());
            OrderItemsData data = ConvertUtil.objectMapper(p, OrderItemsData.class);
            data.setBarcode(product.getBarcode());

            list.add(data);
        }

        return list;

    }

    public void update(Integer id, OrderItemsForm f) throws ApiException {
        OrderItemsPojo p = ConvertUtil.objectMapper(f, OrderItemsPojo.class);

        ValidateUtil.validateOrderItem(p);

        ProductPojo product = pService.get(f.getBarcode());
        p.setProductId(product.getId());

        service.update(id, p);
    }
}
