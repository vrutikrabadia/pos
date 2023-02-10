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
import com.increff.pos.util.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/products")
public class ProductApiController {

    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Adds product")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm productForm) throws ApiException {
        dto.add(productForm);
    }

    @ApiOperation(value = "Adds products in bulk")
    @RequestMapping(path = "/bulk-add", method = RequestMethod.POST)
    public void bulkAdd(@RequestBody List<ProductForm> productFormList) throws ApiException {
        dto.bulkAdd(productFormList);
    }

    @ApiOperation(value = "Gets product by id")
    @RequestMapping(path = "/{productId}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer productId) throws ApiException {
        return dto.get(productId);
    }

    @ApiOperation(value = "Gets all products")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public SelectData<ProductData> getAll(@RequestParam Integer draw,
            @RequestParam Integer start, @RequestParam Integer length,
            @RequestParam(value = "search[value]") String searchValue) throws ApiException {
        return dto.getAll(start, length, draw, searchValue);
    }

    @ApiOperation(value = "Update product by id")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void update(@RequestBody ProductForm productForm) throws ApiException {
        dto.update(productForm);
    }

}
