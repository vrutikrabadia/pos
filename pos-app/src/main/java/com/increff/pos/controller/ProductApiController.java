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

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.SelectData;
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

    @ApiOperation(value = "Adds products in bulk")
    @RequestMapping(path = "/products/bulk-add", method = RequestMethod.POST)
    public void bulkAdddd(@RequestBody List<ProductForm> list)throws ApiException{
        dto.bulkAdd(list);
    }

    @ApiOperation(value = "Gets product by id")
    @RequestMapping(path = "/products/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id)throws ApiException{
        return dto.get(id);
    }

    @ApiOperation(value = "Gets all products")
    @RequestMapping(path = "/products", method = RequestMethod.GET)
    public SelectData<ProductData> getAll(@RequestParam(defaultValue="1") Integer draw, @RequestParam(defaultValue="0") Integer start, @RequestParam(defaultValue="20") Integer length, @RequestParam(value="search[value]") Optional<String> searchValue) throws ApiException{
        return dto.getAll(start, length, draw, searchValue);
    }

    @ApiOperation(value = "Update product by id")
    @RequestMapping(path = "/products/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm form)throws ApiException{
        dto.update(id, form);
    }

}
