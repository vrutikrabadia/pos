package com.increff.pos.controller;

import java.util.List;
import java.util.Optional;


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
import com.increff.pos.service.ApiException;

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
    public OrderData add(@RequestBody List<OrderItemsForm> itemsFormList) throws ApiException{
        return dto.add(itemsFormList);
    }


    @ApiOperation(value = "Gets Order by id")
    @RequestMapping(path="/{id}", method = RequestMethod.GET)
    public OrderData get(@PathVariable Integer orderId) throws ApiException{
        return dto.get(orderId);
    }

    @ApiOperation(value="Get Order Items by order id")
    @RequestMapping(path="/{id}/items", method = RequestMethod.GET)
    public List<OrderItemsData> getByOrderId(@PathVariable Integer orderId) throws ApiException{
        return dto.getByOrderId(orderId);
    }

    @ApiOperation(value = "Gets All orders")
    @RequestMapping(path="", method = RequestMethod.GET)
    public SelectData<OrderData> getAll(@RequestParam(defaultValue="1") Integer draw, @RequestParam(defaultValue="0") Integer start, @RequestParam(defaultValue="20") Integer length, @RequestParam(value="search[value]") Optional<String> searchValue) throws ApiException{
        
        return dto.getAll(start, length, draw, searchValue);
    }

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path="/{id}/invoice", method = RequestMethod.GET)
    public String generateInvoice(@PathVariable Integer id) throws Exception{
         return dto.generateInvoice(id);
    }
}
