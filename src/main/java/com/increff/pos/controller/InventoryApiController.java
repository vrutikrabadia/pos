package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {
    
    @Autowired
    private InventoryDto dto;


    @ApiOperation(value="Adds Inventory")
    @RequestMapping(path="/api/inventory", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm form) throws ApiException{
        dto.add(form);
    }

    @ApiOperation(value = "Get inventory by id")
    @RequestMapping(path="/api/inventory/{barcode}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable String barcode) throws ApiException{
        return dto.get(barcode);
    }

    @ApiOperation(value = "Get inventory ")
    @RequestMapping(path="/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException{
        return dto.getAll();
    }

    @ApiOperation(value = "Update inventory by barcode")
    @RequestMapping(path="/api/inventory", method = RequestMethod.PUT)
    public void update(@RequestBody InventoryForm f) throws ApiException{
        dto.update(f);
    }

}
