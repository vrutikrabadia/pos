package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {
    
    @Autowired
    private OrderDto dto;

    @ApiOperation(value="Creates order")
    @RequestMapping(path="/api/orders", method = RequestMethod.POST)
    public OrderData add(@RequestBody OrderForm form) throws ApiException{
        return dto.add(form);
    }


    @ApiOperation(value = "Gets Order by id")
    @RequestMapping(path="/api/orders/{id}", method = RequestMethod.GET)
    public OrderData get(@PathVariable int id) throws ApiException{
        return dto.get(id);
    }

    @ApiOperation(value = "Gets All orders")
    @RequestMapping(path="/api/orders", method = RequestMethod.GET)
    public List<OrderData> getAll() throws ApiException{
        return dto.getAll();
    }

    @ApiOperation(value = "Updates Order by id")
    @RequestMapping(path="/api/orders/{id}", method = RequestMethod.PUT)
    public OrderData put(@PathVariable int id, @RequestBody OrderForm form) throws ApiException{
        return dto.update(id, form);
    }
}