package com.increff.pos.dto;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.service.ApiException;

@Component
public class TestUtil {
    
    private static BrandDto bDto;
    private static ProductDto pDto;

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;

    @PostConstruct
    private void init(){
        bDto = this.brandDto;
        pDto = this.productDto;
    }

    public static BrandForm getBrandForm(String brand, String category){
        BrandForm f = new BrandForm();
        f.setBrand(brand);
        f.setCategory(category);

        return f;
    }

    public static ProductForm getProductForm(String barcode, String brand, String category, String name, Double mrp){
        ProductForm f = new ProductForm();
        f.setBarcode(barcode);
        f.setBrand(brand);
        f.setCategory(category);
        f.setMrp(mrp);
        f.setName(name);

        return f;
    }

    public static InventoryForm getInventoryForm(String barcode, Integer quantity){
        InventoryForm f = new InventoryForm();
        f.setBarcode(barcode);
        f.setQuantity(quantity);
        
        return f;
    }

    public static void addBrand(String brand, String category) throws ApiException{
        BrandForm f = getBrandForm(brand, category);
        bDto.add(f);
    }

    public static void addProduct(String barcode, String brand, String category, String name, Double mrp) throws ApiException {
        ProductForm f1 = TestUtil.getProductForm(barcode, brand, category, name, mrp);
        pDto.add(f1);
    }

}
