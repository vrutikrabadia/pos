package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderItemsDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api")
public class OrderItemsApiController {


    @Autowired
    private OrderItemsDto dto;

    @ApiOperation(value="Adds Order Items")
    @RequestMapping(path="/orders/items", method = RequestMethod.POST)
    public OrderData add(@RequestBody List<OrderItemsForm> form) throws ApiException{
        return dto.add(form);
    }

    @ApiOperation(value="Adds Order Items to exisiting order")
    @RequestMapping(path="/orders/{id}/items", method = RequestMethod.PUT)
    public void add(@PathVariable Integer id,@RequestBody OrderItemsForm form) throws ApiException{
        dto.addToExisitingOrder(id,form);
    }

    @ApiOperation(value="Get Order Items")
    @RequestMapping(path="/orders/items/{id}", method = RequestMethod.GET)
    public OrderItemsData get(@PathVariable Integer id) throws ApiException{
        return dto.getById(id);
    }

    @ApiOperation(value="Get Order Items by order id")
    @RequestMapping(path="/orders/{id}/items", method = RequestMethod.GET)
    public List<OrderItemsData> getByOrderId(@PathVariable Integer id) throws ApiException{
        return dto.getByOrderId(id);
    }

    @ApiOperation(value="Updates order item by item id")
    @RequestMapping(path="/orders/items/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody OrderItemsForm f) throws ApiException{
        dto.update(id, f);
    }
}
