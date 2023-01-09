package com.increff.pos.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;

@Component
public class ConvertUtil {
    private static BrandService bService;
    
    @Autowired
    BrandService brandService;

    @PostConstruct
    private void init(){
        bService = this.brandService;
    }

	public static BrandPojo convertBrandFormToPojo(BrandForm f){
        BrandPojo p = new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }

    public static BrandData convertBrandPojoToData(BrandPojo p){
        BrandData d = new BrandData();
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        d.setId(p.getId());
        return d;
    }

    public static ProductPojo convertProductFormToPojo(ProductForm f) throws ApiException{
        ProductPojo p = new ProductPojo();
        p.setBarCode(f.getBarCode());
        p.setName(f.getName());
        p.setMrp(f.getMrp());
        BrandPojo bp = bService.get(f.getBrand(), f.getCategory());
        p.setBrandCat(bp.getId());
        return p;
    }

    public static ProductData convertProductPojoToData(ProductPojo p) throws ApiException{
        ProductData d = new ProductData();
        d.setBarCode(p.getBarCode());
        d.setBrandCat(p.getBrandCat());
        d.setId(p.getId());
        d.setMrp(p.getMrp());
        d.setName(p.getName());
        BrandPojo bp = bService.get(p.getBrandCat());
        d.setBrand(bp.getBrand());
        d.setCategory(bp.getCategory());
        return d;
    }

    public static UserData convertUserPojoToData(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		return d;
	}

	public static UserPojo convertUserFormToPojo(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		p.setRole(f.getRole());
		p.setPassword(f.getPassword());
		return p;
	}

}
