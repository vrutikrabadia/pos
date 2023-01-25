package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.service.ApiException;

public class ProductDtoTest extends AbstractUnitTest{
    
    
    @Autowired
    private ProductDto dto;

    private Optional<String> empty = Optional.empty();


    @Test
    public void testAddAndGet() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1A3T5tq8";
        String name = "naMe1";
        Double mrp = 18.88;

        ProductForm form = TestUtil.getProductForm(barcode, brand, category, name, mrp);

        dto.add(form);

        String barcodeNormalised = "1a3t5tq8";
        String nameNormalised = "name1";

        List<ProductData> d = dto.getAll(0,1,1,empty).getData();

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
        Double mrp = 18.88;

        ProductForm form = TestUtil.getProductForm(barcode, name, barcode, name, mrp);
        try{
            dto.add(form);
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
        Double mrp = 18.88;

        ProductForm f1 = TestUtil.getProductForm(barcode, brand, category, name, mrp);
        
        dto.add(f1);

        List<ProductData> d = dto.getAll(0,1,1,empty).getData();

        String barcode1 = "1a3t5tq7";
        String name1 = "name2";
        Double mrp1 = 25.36;

        f1.setBrand(brand1);
        f1.setCategory(category1);
        f1.setName(name1);
        f1.setBarcode(barcode1);
        f1.setMrp(mrp1);

        dto.update(d.get(0).getId(), f1);

        d = dto.getAll(0,1,1, empty).getData();

        assertEquals(d.get(0).getBarcode(), barcode1);
        assertEquals(d.get(0).getBrand(), brand1);
        assertEquals(d.get(0).getCategory(), category1);
        assertEquals(d.get(0).getName(), name1);
        assertEquals(String.valueOf(d.get(0).getMrp()), String.valueOf(mrp1));
    

    }


    @Test
    public void testGetById() throws ApiException{
        TestUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "p1", 10.00);
        List<ProductData> d = dto.getAll(0,1,1,empty).getData();
        ProductData p = dto.get(d.get(0).getId());
        assertEquals(p.getBarcode(), d.get(0).getBarcode());
    }

    @Test
    public void testGetAll() throws ApiException{
        String brand  = "brand1";
        String category = "category1";

       TestUtil.addBrand(brand, category);

        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        ProductForm form = TestUtil.getProductForm(barcode, brand, category, name, mrp);
        dto.add(form);
    
        String barcode1 = "1a3t5tq5";
        String name1 = "name2";
        Double mrp1 = 18.88;

        ProductForm form1 = TestUtil.getProductForm(barcode1, brand, category, name1, mrp1);
        dto.add(form1);

        List<ProductData> d = dto.getAll(0,10,1,empty).getData();


        assertEquals(d.size(), 2);

    }

    @Test
    public void duplicateBarCode() throws ApiException{

        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        ProductForm form = TestUtil.getProductForm(barcode, brand, category, name, mrp);
        dto.add(form);
        
        String barcode1 = "1a3t5tq8";
        String name1 = "name2";
        Double mrp1 = 18.88;

        ProductForm form1 = TestUtil.getProductForm(barcode1, brand, category, name1, mrp1);
        try{
            dto.add(form1);
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
        Double mrp = -18.88;

        ProductForm form = TestUtil.getProductForm(barcode, brand, category, name, mrp);

        try{
            dto.add(form);
        }
        catch(ConstraintViolationException e){
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
        Double mrp = 18.88;

        ProductForm f1 = TestUtil.getProductForm(barcode, brand, category, name, mrp);
        
        dto.add(f1);

        List<ProductData> d = dto.getAll(0,1, 1, empty).getData();

        String barcode1 = "1a3t5tq7";
        String name1 = "name2";
        Double mrp1 = -25.36;

        f1.setBrand(brand1);
        f1.setCategory(category1);
        f1.setName(name1);
        f1.setBarcode(barcode1);
        f1.setMrp(mrp1);

        try{
            dto.update(d.get(0).getId(), f1);
        }
        catch(ConstraintViolationException e){
            return;
        }
        fail();
    }

    @Test
    public void testSearchFeild(){
        String brand  = "brand1";
        String category = "category1";
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        TestUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        SelectData<ProductData> list = new SelectData<ProductData>();

        try{
            list = dto.getAll(0, 5, 0, Optional.of("1a3"));
        }
        catch(ApiException e){
            fail();
        }

        assertEquals(1, list.getData().size());

        try{
            list = dto.getAll(0, 5, 0, Optional.of("brand1"));
        }
        catch(ApiException e){
            fail();
        }

        assertEquals(1, list.getData().size());

        try{
            list = dto.getAll(0, 5, 0, Optional.of("cate"));
        }
        catch(ApiException e){
            fail();
        }

        assertEquals(1, list.getData().size());

        try{
            list = dto.getAll(0, 5, 0, Optional.of("name"));
        }
        catch(ApiException e){
            fail();
        }

        assertEquals(1, list.getData().size());

        try{
            list = dto.getAll(0, 5, 0, Optional.of("xyz"));
        }
        catch(ApiException e){
            fail();
        }

        assertEquals(0, list.getData().size());

    }


    //TODO: Add tests for poduct Bulk Add

    
}
