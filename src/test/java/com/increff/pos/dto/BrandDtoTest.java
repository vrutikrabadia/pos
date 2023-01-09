package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        
        BrandForm f = TestUtil.getBrandForm(brand, category);

        dto.add(f);

        List<BrandData> d = dto.getAll();

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

        List<BrandData> d = dto.getAll();

        dto.update(d.get(0).getId(), f);

        d = dto.getAll();

        assertEquals(d.get(0).getBrand(), "brand2");
        assertEquals(d.get(0).getCategory(), "category2");
        
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandForm f1 = TestUtil.getBrandForm("brand1", "category1");
        BrandForm f2 = TestUtil.getBrandForm("brand2", "category2");
        BrandForm f3 = TestUtil.getBrandForm("brand3", "category3");

        dto.add(f1);
        dto.add(f2);
        dto.add(f3);

        List<BrandData> d = dto.getAll();

        assertEquals(d.size(), 3);
    }

    @Test 
    public void testDupicateInsertion() throws ApiException{
        boolean thrown = false;
        BrandForm f1 = TestUtil.getBrandForm("brand1", "category1");
        dto.add(f1);

        try{
            dto.add(f1);
        }
        catch(ApiException e){
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Test
    public void testGetByBrandCategory() throws ApiException{
        String brand  = "brand1";
        String category = "category1";
        
        BrandForm f = TestUtil.getBrandForm(brand, category);

        dto.add(f);

        BrandData d = dto.get(brand, category);

        if(d==null){
            assertTrue(false);
        }
        else{
            assertEquals(d.getCategory(), f.getCategory());
            assertEquals(d.getBrand(), f.getBrand());
        }
        
    }


    @Test
    public void testNormalise() throws ApiException{
        String brand  = "Brand1 ";
        String category = " caTegory1";
        
        BrandForm f = TestUtil.getBrandForm(brand, category);

        dto.add(f);

        List<BrandData> d = dto.getAll();

        assertEquals(d.get(0).getBrand(), "brand1");
        assertEquals(d.get(0).getCategory(), "category1");
    }
}
