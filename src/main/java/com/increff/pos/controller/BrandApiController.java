package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {
    

    @Autowired
    private BrandService service;

    @ApiOperation(value = "Adds brand/category")
    @RequestMapping(path = "/api/brands", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm form) throws ApiException{
        BrandPojo p = convert(form);
        service.add(p);
    }

    @ApiOperation(value = "Deletes brand/category by id")
    @RequestMapping(path = "/api/brands/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id){
        service.delete(id);
    }

    @ApiOperation(value = "Get brand/category by id")
    @RequestMapping(path = "/api/brands/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException{
        BrandPojo p = service.get(id);
        return convert(p);
    }


    @ApiOperation(value = "Get all brand/category")
    @RequestMapping(path = "/api/brands", method = RequestMethod.GET)
    public List<BrandData> get() throws ApiException{
        List<BrandPojo> list = service.getAll();
        List<BrandData> list1 = new ArrayList<BrandData>();
        for(BrandPojo p:list){
            list1.add(convert(p));
        }

        return list1;
    }

    @ApiOperation(value = "Get brand/category by brand/category")
    @RequestMapping(path = "/api/brands/{brand}/{category}", method = RequestMethod.GET)
    public BrandData get(@PathVariable String brand, @PathVariable String category) throws ApiException{
        BrandPojo p = service.get(brand, category);
        return convert(p);
    }

    @ApiOperation(value = "Update brand/category by id")
    @RequestMapping(path = "/api/brands/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException{
        BrandPojo p = convert(form);
        service.update(id, p);
    }

    private static BrandPojo convert(BrandForm f){
        BrandPojo p = new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }

    private static BrandData convert(BrandPojo p){
        BrandData d = new BrandData();
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        d.setId(p.getId());
        return d;
    }
}
