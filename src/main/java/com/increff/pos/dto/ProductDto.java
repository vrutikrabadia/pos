package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;

@Component
public class ProductDto {
    
    @Autowired
    private ProductService service;

    public void add(ProductForm form) throws ApiException{
        ProductPojo p = ConvertUtil.convertProductFormToPojo(form);

        StringUtil.normaliseProduct(p);

        if(StringUtil.isEmpty(p.getName())){
            throw new ApiException("Product name cannot be empty");
        }
        if(StringUtil.isEmpty(p.getBarCode()) || p.getBarCode().length() != 8){
            throw new ApiException("Please provide a valid barcode(length 8)");
        }

        if(service.checkBarCode(0, p.getBarCode())){
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        if(p.getMrp()<0){
            throw new ApiException("MRP should be non negative");
        }

        service.add(p);
    }

    public ProductData get(int id) throws ApiException{
        ProductPojo p = service.get(id);

        return ConvertUtil.convertProductPojoToData(p);
    }

    public List<ProductData> getAll() throws ApiException{
        List<ProductPojo> list = service.getAll();
        List<ProductData> list1 = new ArrayList<ProductData>();
        for(ProductPojo p: list){
            list1.add(ConvertUtil.convertProductPojoToData(p));
        }

        return list1;
    }

    public void update(int id, ProductForm f) throws ApiException{
        ProductPojo p = ConvertUtil.convertProductFormToPojo(f);

        StringUtil.normaliseProduct(p);

        if(StringUtil.isEmpty(p.getName())){
            throw new ApiException("Product name cannot be empty");
        }
        if(StringUtil.isEmpty(p.getBarCode()) || p.getBarCode().length() != 8){
            throw new ApiException("Please provide a valid barcode(length 8)");
        }

        if(service.checkBarCode(id,p.getBarCode())){
            throw new ApiException("DUPLICATE BARCODE: Product with barcode already exists.");
        }

        if(p.getMrp()<0){
            throw new ApiException("MRP should be non negative");
        }

        service.update(id, p);
    }


}
