package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

        String barcode = "1A3T5tq8";
        String name = "naMe1";
        double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);

        String barcodeNormalised = "1a3t5tq8";
        String nameNormalised = "name1";

        List<ProductData> d = dto.getAll(0,1,1).getData();

        assertEquals(d.get(0).getBarcode(), barcodeNormalised);
        assertEquals(d.get(0).getBrand(), brand);
        assertEquals(d.get(0).getCategory(), category);
        assertEquals(d.get(0).getName(), nameNormalised);
        assertEquals(String.valueOf(d.get(0).getMrp()), String.valueOf(mrp));
    }

    @Test
    public void testAddProductWithouBrandAndCategory() throws ApiException{
        
        String barcode = "1A3T5tq8";
        String name = "naMe1";
        double mrp = 18.88;

        try{
            TestUtil.addProduct(barcode, name, barcode, name, mrp);
        }

        catch(ApiException e){
            return;
        }
        fail();
    }

    @Test
    public void testUpdate() throws ApiException{
        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String brand1  = "brand2";
        String category1 = "category2";

        TestUtil.addBrand(brand1, category1);

        String barcode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        ProductForm f1 = TestUtil.getProductForm(barcode, brand, category, name, mrp);
        
        dto.add(f1);

        List<ProductData> d = dto.getAll(0,1,1).getData();

        String barcode1 = "1a3t5tq7";
        String name1 = "name2";
        double mrp1 = 25.36;

        f1.setBrand(brand1);
        f1.setCategory(category1);
        f1.setName(name1);
        f1.setBarcode(barcode1);
        f1.setMrp(mrp1);

        dto.update(d.get(0).getId(), f1);

        d = dto.getAll(0,1,1).getData();

        assertEquals(d.get(0).getBarcode(), barcode1);
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

        String barcode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);
    
    
        String barcode1 = "1a3t5tq5";
        String name1 = "name2";
        double mrp1 = 18.88;

        TestUtil.addProduct(barcode1, brand, category, name1, mrp1);

        List<ProductData> d = dto.getAll(0,10,1).getData();


        assertEquals(d.size(), 2);

    }

    @Test
    public void duplicateBarCode() throws ApiException{

        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);
    
        
        String barcode1 = "1a3t5tq8";
        String name1 = "name2";
        double mrp1 = 18.88;

        
        try{
            TestUtil.addProduct(barcode1, brand, category, name1, mrp1);
        }
        catch(ApiException e){
            return;
        }
        fail();
        
    }

    @Test
    public void testAddNegativeMrp() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1A3T5tq8";
        String name = "naMe1";
        double mrp = -18.88;

        try{
            TestUtil.addProduct(barcode, brand, category, name, mrp);
        }
        catch(ApiException e){
            return;
        }
        fail();
    }

    @Test
    public void testUpdateNegativeMrp() throws ApiException{

        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String brand1  = "brand2";
        String category1 = "category2";

        TestUtil.addBrand(brand1, category1);

        String barcode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        ProductForm f1 = TestUtil.getProductForm(barcode, brand, category, name, mrp);
        
        dto.add(f1);

        List<ProductData> d = dto.getAll(0,1, 1).getData();

        String barcode1 = "1a3t5tq7";
        String name1 = "name2";
        double mrp1 = -25.36;

        f1.setBrand(brand1);
        f1.setCategory(category1);
        f1.setName(name1);
        f1.setBarcode(barcode1);
        f1.setMrp(mrp1);

        try{
            dto.update(d.get(0).getId(), f1);
        }
        catch(ApiException e){
            return;
        }
        fail();
    }

    
}
