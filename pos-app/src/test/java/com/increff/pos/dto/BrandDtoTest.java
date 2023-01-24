package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidateUtil;

public class BrandDtoTest extends AbstractUnitTest{

    @Autowired 
    private BrandDto dto;
    private Optional<String> empty = Optional.empty();


    
    @Test
    public void testAddAndGet() throws ApiException{
        String brand  = "Brand1 ";
        String category = " caTegory1";

        BrandForm form = TestUtil.getBrandForm(brand, category);
        
        dto.add(form);

        List<BrandData> d = dto.getAll(0,1,1,empty).getData();

        assertEquals(d.get(0).getBrand(), "brand1");
        assertEquals(d.get(0).getCategory(), "category1");
    }

    @Test
    public void testUpdate() throws ApiException{
        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);
        BrandForm f = TestUtil.getBrandForm(brand, category);

        f.setCategory("category2");
        f.setBrand("brand2");

        
        Optional<String> empty = Optional.empty();
        List<BrandData> d = dto.getAll(0,1,1, empty).getData();

        dto.update(d.get(0).getId(), f);

        d = dto.getAll(0,1,1, empty).getData();

        assertEquals(d.get(0).getBrand(), "brand2");
        assertEquals(d.get(0).getCategory(), "category2");
        
    }

    @Test
    public void testGetAll() throws ApiException{
        TestUtil.addBrand("brand1", "category1");
        TestUtil.addBrand("brand2", "category2");
        TestUtil.addBrand("brand3", "category3");

        List<BrandData> d = dto.getAll(0,5,1,empty).getData();

        assertEquals(d.size(), 3);
    }

    @Test 
    public void testDupicateInsertion() throws ApiException{
        
        TestUtil.addBrand("brand1", "category1");

        BrandForm f = TestUtil.getBrandForm("brand1", "category1");
        

        try{
            dto.add(f);
        }
        catch(ApiException e){
            return;
        }
        fail();        
    }

    @Test
    public void testGetById() throws ApiException{
        String brand  = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        List<BrandData> d = dto.getAll(0,1,1,empty).getData();

        BrandData d1 = dto.get(d.get(0).getId());

        assertEquals(d1.getBrand(), brand);
        assertEquals(d1.getCategory(), category);
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
    public void testBrandValidation(){
        List<BrandForm> brandList = new ArrayList<BrandForm>();

        brandList.add(TestUtil.getBrandForm(null, "hello"));

        try{
            ValidateUtil.validateList(brandList);
        }catch(ApiException e){
            brandList.add(TestUtil.getBrandForm("", "hello"));
            try{
                ValidateUtil.validateList(brandList);
            }catch(ApiException e1){
                brandList.add(TestUtil.getBrandForm("hello", null));
                try{
                    ValidateUtil.validateList(brandList);
                }catch(ApiException e2){
                    brandList.add(TestUtil.getBrandForm( "hello", ""));
                    try{
                        ValidateUtil.validateList(brandList);
                    }catch(ApiException e3){
                        brandList.add(TestUtil.getBrandForm("", ""));
                        return;
                    }                   
                }
            }
        }

        fail();
    }

    @Test
    public void testFileDuplicate(){
        
        List<BrandForm> brandList = new ArrayList<BrandForm>();

        BrandForm f1 = TestUtil.getBrandForm("b1", "c1");
        brandList.add(f1);

        BrandForm f2 = TestUtil.getBrandForm("b2", "c2");
        brandList.add(f2);

        List<BrandPojo> pojoList = brandList.stream().map(e->ConvertUtil.objectMapper(e, BrandPojo.class)).collect(Collectors.toList());

        try{
            dto.checkFileDuplications(pojoList);
        }catch(ApiException e){
            fail();
        }

        brandList.add(f2);
        pojoList = brandList.stream().map(e->ConvertUtil.objectMapper(e, BrandPojo.class)).collect(Collectors.toList());

        try{
            dto.checkFileDuplications(pojoList);
        }catch(ApiException e){
            return;
        }
        fail();
    }


    @Test
    public void testDbDuplicate(){
        TestUtil.addBrand("b2", "c2");

        List<BrandForm> brandList = new ArrayList<BrandForm>();

        BrandForm f1 = TestUtil.getBrandForm("b1", "c1");
        brandList.add(f1);

        BrandForm f2 = TestUtil.getBrandForm("b2", "c2");
        brandList.add(f2);

        List<BrandPojo> pojoList = brandList.stream().map(e->ConvertUtil.objectMapper(e, BrandPojo.class)).collect(Collectors.toList());

        try{
            dto.checkDbDuplicate(pojoList);
        }catch(ApiException e){
            return;
        }

        fail();
    }

    @Test
    public void testBulkAdd(){
        List<BrandForm> brandList = new ArrayList<BrandForm>();

        BrandForm f1 = TestUtil.getBrandForm("b1", "c1");
        brandList.add(f1);

        BrandForm f2 = TestUtil.getBrandForm("b2", "c2");
        brandList.add(f2);

        try{
            dto.bulkAdd(brandList);
        }catch(ApiException e){
            fail();
        }

        SelectData<BrandData> dataList = dto.getAll(0, 3, 0, empty);

        assertEquals(2, dataList.getData().size());
    }

}
