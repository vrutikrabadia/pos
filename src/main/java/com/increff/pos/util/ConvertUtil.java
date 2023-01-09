package com.increff.pos.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;

@Component
public class ConvertUtil {
    private static BrandService bService;
    private static ProductService pService;
    
    @Autowired
    BrandService brandService;
    @Autowired
    ProductService productService;

    @PostConstruct
    private void init(){
        bService = this.brandService;
        pService = this.productService;
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

    public static InventoryData convertInventoryPojoToData(InventoryPojo p) throws ApiException{
        InventoryData d = new InventoryData();
        d.setQuantity(p.getQuantity());
        ProductPojo p1 = pService.get(p.getId());
        d.setBarCode(p1.getBarCode());
        return d;
    }

    public static InventoryPojo convertInventoryFormtoPojo(InventoryForm f) throws ApiException{
        InventoryPojo p = new InventoryPojo();
        p.setQuantity(f.getQuantity());
        f.setBarCode(StringUtil.toLowerCase(f.getBarCode()));
        ProductPojo p1 = pService.get(f.getBarCode());
        p.setId(p1.getId());

        return p;
    }

}
