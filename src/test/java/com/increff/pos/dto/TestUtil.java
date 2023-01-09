package com.increff.pos.dto;

import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.ProductForm;

public class TestUtil {
    
    

    public static BrandForm getBrandForm(String brand, String category){
        BrandForm f = new BrandForm();
        f.setBrand(brand);
        f.setCategory(category);

        return f;
    }

    public static ProductForm getProductForm(String barCode, String brand, String category, String name, double mrp){
        ProductForm f = new ProductForm();
        f.setBarCode(barCode);
        f.setBrand(brand);
        f.setCategory(category);
        f.setMrp(mrp);
        f.setName(name);

        return f;
    }

}
