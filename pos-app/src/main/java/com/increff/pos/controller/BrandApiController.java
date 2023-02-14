package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/brands")
public class BrandApiController {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds a Brand and Category.")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm brandForm) throws ApiException {
        dto.add(brandForm);
    }

    @ApiOperation(value = "Adds Brands and Categories in bulk.")
    @RequestMapping(path = "/bulk-add", method = RequestMethod.POST)
    public void bulkAdd(@RequestBody List<BrandForm> brandFormList) throws ApiException {
        dto.bulkAdd(brandFormList);
    }

    @ApiOperation(value = "Gets Brand and Category by Id.")
    @RequestMapping(path = "/{bCatId}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer bCatId) throws ApiException {
        return dto.getById(bCatId);
    }

    @ApiOperation(value = "Gets all Brand and Category.")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public SelectData<BrandData> get(@RequestParam Integer draw, @RequestParam Integer start, @RequestParam Integer length, @RequestParam(value = "search[value]") String searchValue)
            throws ApiException {
        return dto.getAllPaginated(start, length, draw, searchValue);
    }

    @ApiOperation(value = "Gets Brand and Category by names.")
    @RequestMapping(path = "/{brand}/categories/{category}", method = RequestMethod.GET)
    public BrandData get(@PathVariable String brand, @PathVariable String category) throws ApiException {
        return dto.get(brand, category);
    }

    @ApiOperation(value = "Updates Brand and Category by Id.")
    @RequestMapping(path = "/{bCatId}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer bCatId, @RequestBody BrandForm brandForm) throws ApiException {
        dto.update(bCatId, brandForm);
    }
}
