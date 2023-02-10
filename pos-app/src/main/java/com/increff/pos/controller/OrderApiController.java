package com.increff.pos.controller;

import java.util.List;
import java.util.Optional;


import com.increff.pos.model.form.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemsData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.OrderItemsForm;
import com.increff.pos.util.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/orders")
public class OrderApiController {
    
    @Autowired
    private OrderDto dto;

    @ApiOperation(value="Creates a order.")
    @RequestMapping(path="", method = RequestMethod.POST)
    public OrderData add(@RequestBody OrderForm orderForm) throws ApiException{
        return dto.add(orderForm);
    }


    @ApiOperation(value = "Gets Order by id")
    @RequestMapping(path="/{orderId}", method = RequestMethod.GET)
    public OrderData get(@PathVariable Integer orderId) throws ApiException{
        return dto.get(orderId);
    }

    @ApiOperation(value="Get Order Items by order id")
    @RequestMapping(path="/{orderId}/items", method = RequestMethod.GET)
    public List<OrderItemsData> getByOrderId(@PathVariable Integer orderId) throws ApiException{
        return dto.getByOrderId(orderId);
    }

    @ApiOperation(value = "Gets All orders")
    @RequestMapping(path="", method = RequestMethod.GET)
    public SelectData<OrderData> getAll(@RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length, @RequestParam(value="search[value]") String searchValue) throws ApiException{
        
        return dto.getAll(start, length, draw, searchValue);
    }

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path="/{orderId}/invoice", method = RequestMethod.GET)
    public String generateInvoice(@PathVariable Integer orderId) throws ApiException{
         return dto.generateInvoice(orderId);
    }
}
