package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderItemsDto;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderItemsApiController {


    @Autowired
    private OrderItemsDto dto;

    @ApiOperation(value="Adds Order Items")
    @RequestMapping(path="/api/orders/{id}/items", method = RequestMethod.POST)
    public void add(@RequestBody List<OrderItemsForm> form, @PathVariable int orderId) throws ApiException{
        dto.add(orderId, form);
    }

    @ApiOperation(value="Get Order Items")
    @RequestMapping(path="/api/orders/items/{id}", method = RequestMethod.GET)
    public OrderItemsData get(@PathVariable int id) throws ApiException{
        return dto.getById(id);
    }

    @ApiOperation(value="Get Order Items by order id")
    @RequestMapping(path="/api/orders/{id}/items", method = RequestMethod.GET)
    public List<OrderItemsData> getByOrderId(@PathVariable int id) throws ApiException{
        return dto.getByOrderId(id);
    }

    @ApiOperation(value="Updates order item by item id")
    @RequestMapping(path="/api/orders/items/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody OrderItemsForm f) throws ApiException{
        dto.update(id, f);
    }
}
