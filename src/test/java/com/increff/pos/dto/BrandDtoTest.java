package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.service.ApiException;

public class BrandDtoTest extends AbstractUnitTest{

    @Autowired 
    BrandDto dto;

    @Test
    public void testAddAndGet() throws ApiException{
        String brand  = "brand1";
        String category = "category1";
        
        TestUtil.addBrand(brand, category);

        List<BrandData> d = dto.getAll(0,1);

        assertEquals(d.get(0).getBrand(), brand);
        assertEquals(d.get(0).getCategory(), category);
    }

    @Test
    public void testUpdate() throws ApiException{
        String brand  = "brand1";
        String category = "category1";

        BrandForm f = TestUtil.getBrandForm(brand, category);

        dto.add(f);

        f.setCategory("category2");
        f.setBrand("brand2");

        List<BrandData> d = dto.getAll(0,1);

        dto.update(d.get(0).getId(), f);

        d = dto.getAll(0,1);

        assertEquals(d.get(0).getBrand(), "brand2");
        assertEquals(d.get(0).getCategory(), "category2");
        
    }

    @Test
    public void testGetAll() throws ApiException{
        TestUtil.addBrand("brand1", "category1");
        TestUtil.addBrand("brand2", "category2");
        TestUtil.addBrand("brand3", "category3");

        List<BrandData> d = dto.getAll(0,5);

        assertEquals(d.size(), 3);
    }

    @Test 
    public void testDupicateInsertion() throws ApiException{
        TestUtil.addBrand("brand1", "category1");
        

        try{
            TestUtil.addBrand("brand1", "category1");
        }
        catch(ApiException e){
            fail();
        }
        
    }

    @Test
    public void testGetByBrandCategory() throws ApiException{
        String brand  = "brand1";
        String category = "category1";

        
        TestUtil.addBrand(brand, category);

        BrandData d = dto.get(brand, category);

        if(d==null){
            assertTrue(false);
        }
        else{
            assertEquals(d.getCategory(), category);
            assertEquals(d.getBrand(), brand);
        }
        
    }


    @Test
    public void testNormalise() throws ApiException{
        String brand  = "Brand1 ";
        String category = " caTegory1";
        
        TestUtil.addBrand(brand, category);

        List<BrandData> d = dto.getAll(0,1);

        assertEquals(d.get(0).getBrand(), "brand1");
        assertEquals(d.get(0).getCategory(), "category1");
    }
}
