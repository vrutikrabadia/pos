package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;

@Component
public class BrandDto {
    
    @Autowired
    private BrandService service;

    public void add(BrandForm f) throws ApiException{
        BrandPojo p = ConvertUtil.convertBrandFormToPojo(f);
        StringUtil.normaliseBrand(p);
        if(StringUtil.isEmpty(p.getBrand()) || StringUtil.isEmpty(p.getCategory())){
            throw new ApiException("Brand or category cannot be empty");
        }
        if(service.checkDuplicate(p)){
            throw new ApiException("brand/category already exist: "+p.getBrand()+"/"+p.getCategory());
        }
        service.add(p);
    }

    public BrandData get(int id) throws ApiException{
        BrandPojo p = service.get(id);
        return ConvertUtil.convertBrandPojoToData(p);
    }

    public List<BrandData> getAll(){
        List<BrandPojo> list = service.getAll();
        List<BrandData> list1 = new ArrayList<BrandData>();
        for(BrandPojo p:list){
            list1.add(ConvertUtil.convertBrandPojoToData(p));
        }

        return list1;

    }

    public BrandData get(String brand, String category) throws ApiException{
        BrandPojo p = service.get(brand, category);

        return ConvertUtil.convertBrandPojoToData(p);
    }

    public void update(int id, BrandForm f) throws ApiException{
        BrandPojo p = ConvertUtil.convertBrandFormToPojo(f);
        StringUtil.normaliseBrand(p);
        if(StringUtil.isEmpty(p.getBrand()) || StringUtil.isEmpty(p.getCategory())){
            throw new ApiException("Brand of category cannot be empty");
        }
        if(service.checkDuplicate(p)){
            throw new ApiException("brand/category already exist: "+p.getBrand()+"/"+p.getCategory());
        }
        service.update(id, p);
    }

}
