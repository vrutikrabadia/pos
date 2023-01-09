package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.service.ApiException;

public class ProductDtoTest extends AbstractUnitTest{
    
    
    @Autowired
    ProductDto dto;
    @Autowired
    BrandDto bDto;


    @Test
    public void testAddAndGet() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1A3T5tq8";
        String name = "naMe1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);

        String barCodeNormalised = "1a3t5tq8";
        String nameNormalised = "name1";

        List<ProductData> d = dto.getAll();

        assertEquals(d.get(0).getBarCode(), barCodeNormalised);
        assertEquals(d.get(0).getBrand(), brand);
        assertEquals(d.get(0).getCategory(), category);
        assertEquals(d.get(0).getName(), nameNormalised);
        assertEquals(String.valueOf(d.get(0).getMrp()), String.valueOf(mrp));
    }

    @Test
    public void testAddProductWithouBrandAndCategory() throws ApiException{
        boolean thrown = false;
        
        String barCode = "1A3T5tq8";
        String name = "naMe1";
        double mrp = 18.88;

        try{
            TestUtil.addProduct(barCode, name, barCode, name, mrp);
        }

        catch(ApiException e){
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Test
    public void testUpdate() throws ApiException{
        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String brand1  = "brand2";
        String category1 = "category2";

        TestUtil.addBrand(brand1, category1);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        ProductForm f1 = TestUtil.getProductForm(barCode, brand, category, name, mrp);
        
        dto.add(f1);

        List<ProductData> d = dto.getAll();

        String barCode1 = "1a3t5tq7";
        String name1 = "name2";
        double mrp1 = 25.36;

        f1.setBrand(brand1);
        f1.setCategory(category1);
        f1.setName(name1);
        f1.setBarCode(barCode1);
        f1.setMrp(mrp1);

        dto.update(d.get(0).getId(), f1);

        d = dto.getAll();

        assertEquals(d.get(0).getBarCode(), barCode1);
        assertEquals(d.get(0).getBrand(), brand1);
        assertEquals(d.get(0).getCategory(), category1);
        assertEquals(d.get(0).getName(), name1);
        assertEquals(String.valueOf(d.get(0).getMrp()), String.valueOf(mrp1));
    

    }


    @Test
    public void testGetAll() throws ApiException{
        String brand  = "brand1";
        String category = "category1";

       TestUtil.addBrand(brand, category);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);
    
    
        String barCode1 = "1a3t5tq5";
        String name1 = "name2";
        double mrp1 = 18.88;

        TestUtil.addProduct(barCode1, brand, category, name1, mrp1);

        List<ProductData> d = dto.getAll();


        assertEquals(d.size(), 2);

    }

    @Test
    public void duplicateBarCode() throws ApiException{
        boolean thrown = false;

        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);
    
        
        String barCode1 = "1a3t5tq8";
        String name1 = "name2";
        double mrp1 = 18.88;

        
        try{
            TestUtil.addProduct(barCode1, brand, category, name1, mrp1);
        }
        catch(ApiException e){
            thrown = true;
        }

        assertTrue(thrown);
        
    }

    @Test
    public void testAddNegativeMrp() throws ApiException{
        boolean thrown = false;
        
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1A3T5tq8";
        String name = "naMe1";
        double mrp = -18.88;

        try{
            TestUtil.addProduct(barCode, brand, category, name, mrp);
        }
        catch(ApiException e){
            thrown = true;
        }

        assertTrue(thrown);;
    }

    @Test
    public void testUpdateNegativeMrp() throws ApiException{
        boolean thrown = false;

        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String brand1  = "brand2";
        String category1 = "category2";

        TestUtil.addBrand(brand1, category1);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        ProductForm f1 = TestUtil.getProductForm(barCode, brand, category, name, mrp);
        
        dto.add(f1);

        List<ProductData> d = dto.getAll();

        String barCode1 = "1a3t5tq7";
        String name1 = "name2";
        double mrp1 = -25.36;

        f1.setBrand(brand1);
        f1.setCategory(category1);
        f1.setName(name1);
        f1.setBarCode(barCode1);
        f1.setMrp(mrp1);

        try{
            dto.update(d.get(0).getId(), f1);
        }
        catch(ApiException e){
            thrown = true;
        }

        assertTrue(thrown);

    }

    
}
