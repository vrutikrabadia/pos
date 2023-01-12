package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.ProductSelectData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api")
public class ProductApiController {
    

    @Autowired
    private ProductDto dto;



    @ApiOperation(value = "Adds product")
    @RequestMapping(path = "/products", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form)throws ApiException{
        dto.add(form);
    }

    @ApiOperation(value = "Gets product by id")
    @RequestMapping(path = "/products/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id)throws ApiException{
        return dto.get(id);
    }

    @ApiOperation(value = "Gets all products")
    @RequestMapping(path = "/products", method = RequestMethod.GET)
    public ProductSelectData getAll(@RequestParam Integer draw,@RequestParam Integer start, @RequestParam Integer length) throws ApiException{
        return dto.getAll(start/length, length, draw);
    }

    @ApiOperation(value = "Update product by id")
    @RequestMapping(path = "/products/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm form)throws ApiException{
        dto.update(id, form);
    }

}
