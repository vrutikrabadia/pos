package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {
    

    @Autowired
    private ProductService service;

    @Autowired
    private BrandService bservice;


    @ApiOperation(value = "Adds product")
    @RequestMapping(path = "/api/products", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form)throws ApiException{
        ProductPojo p = convert(form);
        service.add(p);
    }

    @ApiOperation(value = "Deletes product by id")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id)throws ApiException{
        service.delete(id);
    }

    @ApiOperation(value = "Gets product by id")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id)throws ApiException{
        ProductPojo p = service.get(id);
        return convert(p);
    }

    @ApiOperation(value = "Gets all products")
    @RequestMapping(path = "/api/products", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException{
        List<ProductPojo> list = service.getAll();
        List<ProductData> list1 = new ArrayList<ProductData>();
        for(ProductPojo p: list){
            list1.add(convert(p));
        }

        return list1;
    }

    @ApiOperation(value = "Update product by id")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm form)throws ApiException{
        service.update(id, convert(form));
    }


    private ProductPojo convert(ProductForm f) throws ApiException{
        ProductPojo p = new ProductPojo();
        p.setName(f.getName());
        p.setMrp(f.getMrp());
        BrandPojo bp = bservice.get(f.getBrand(), f.getCategory());
        p.setBrandCat(bp.getId());
        return p;
    }

    private ProductData convert(ProductPojo p) throws ApiException{
        ProductData d = new ProductData();
        d.setBarCode(p.getBarCode());
        d.setBrandCat(p.getBrandCat());
        d.setId(p.getId());
        d.setMrp(p.getMrp());
        d.setName(p.getName());
        BrandPojo bp = bservice.get(p.getBrandCat());
        d.setBrand(bp.getBrand());
        d.setCategory(bp.getCategory());
        return d;
    }
}
