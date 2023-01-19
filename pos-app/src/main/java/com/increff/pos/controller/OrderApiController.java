package com.increff.pos.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

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
import com.increff.pos.model.form.OrderForm;
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

    @ApiOperation(value="Creates order")
    @RequestMapping(path="", method = RequestMethod.POST)
    public OrderData add(@RequestBody List<OrderItemsForm> form) throws ApiException{
        return dto.add(form);
    }


    @ApiOperation(value = "Gets Order by id")
    @RequestMapping(path="/{id}", method = RequestMethod.GET)
    public OrderData get(@PathVariable Integer id) throws ApiException{
        return dto.get(id);
    }

    @ApiOperation(value="Get Order Items by order id")
    @RequestMapping(path="/{id}/items", method = RequestMethod.GET)
    public List<OrderItemsData> getByOrderId(@PathVariable Integer id) throws ApiException{
        return dto.getByOrderId(id);
    }

    @ApiOperation(value = "Gets All orders")
    @RequestMapping(path="", method = RequestMethod.GET)
    public SelectData<OrderData> getAll(@RequestParam(defaultValue="1") Integer draw, @RequestParam(defaultValue="0") Integer start, @RequestParam(defaultValue="20") Integer length, @RequestParam(value="search[value]") Optional<String> searchValue) throws ApiException{
        
        return dto.getAll(start, length, draw, searchValue);
    }

    @ApiOperation(value = "Updates Order by id")
    @RequestMapping(path="/{id}", method = RequestMethod.PUT)
    public OrderData put(@PathVariable Integer id, @RequestBody OrderForm form) throws ApiException{
        return dto.update(id, form);
    }

    @ApiOperation(value = "Sets order non editable")
    @RequestMapping(path="/{id}/finalise", method = RequestMethod.PUT)
    public void finaliseOrder(@PathVariable Integer id) throws ApiException{
        dto.finaliseOrder(id);
    }

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path="/{id}/invoice", method = RequestMethod.GET)
    public void generateInvoice(@PathVariable Integer id, HttpServletResponse response) throws Exception{
        dto.generateInvoice(id, response);
    }
}
