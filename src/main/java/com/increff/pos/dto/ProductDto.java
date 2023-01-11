package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;

@Component
public class ProductDto {

    @Autowired
    private ProductService service;
    @Autowired
    private BrandService bService;

    public void add(ProductForm form) throws ApiException {

        BrandPojo brandP = bService.get(form.getBrand(), form.getCategory());

        ProductPojo p = ConvertUtil.objectMapper(form, ProductPojo.class);
        p.setBrandCat(brandP.getId());

        StringUtil.normaliseProduct(p);

        if (StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Product name cannot be empty");
        }
        if (StringUtil.isEmpty(p.getBarcode()) || p.getBarcode().length() != 8) {
            throw new ApiException("Please provide a valid barcode(length 8)");
        }

        if (service.checkBarCode(0, p.getBarcode())) {
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        if (p.getMrp() < 0) {
            throw new ApiException("MRP should be non negative");
        }

        service.add(p);
    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo p = service.get(id);

        BrandPojo brandP = bService.get(p.getBrandCat());
        ProductData pData = ConvertUtil.objectMapper(p, ProductData.class);

        pData.setBrand(brandP.getBrand());
        pData.setCategory(brandP.getCategory());

        return pData;
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> list = service.getAll();
        List<ProductData> list1 = new ArrayList<ProductData>();
        for (ProductPojo p : list) {

            BrandPojo brandP = bService.get(p.getBrandCat());
            ProductData pData = ConvertUtil.objectMapper(p, ProductData.class);

            pData.setBrand(brandP.getBrand());
            pData.setCategory(brandP.getCategory());

            list1.add(pData);
        }

        return list1;
    }

    public void update(Integer id, ProductForm form) throws ApiException {
        
        BrandPojo brandP = bService.get(form.getBrand(), form.getCategory());

        ProductPojo p = ConvertUtil.objectMapper(form, ProductPojo.class);
        p.setBrandCat(brandP.getId());

        StringUtil.normaliseProduct(p);

        if (StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Product name cannot be empty");
        }
        if (StringUtil.isEmpty(p.getBarcode()) || p.getBarcode().length() != 8) {
            throw new ApiException("Please provide a valid barcode(length 8)");
        }

        if (service.checkBarCode(id, p.getBarcode())) {
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        if (p.getMrp() < 0) {
            throw new ApiException("MRP should be non negative");
        }

        service.update(id, p);
    }

}
