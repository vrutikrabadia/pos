package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.service.ApiException;

public class InventoryDtoTest extends AbstractUnitTest{
    
    @Autowired
    InventoryDto dto;

    @Test
    public void testAddAndGet() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);

        int quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barCode, quantity);

        dto.add(f);

        List<InventoryData> d = dto.getAll();
        
        assertEquals(d.get(0).getBarCode(), barCode);
        assertEquals(d.get(0).getQuantity(), quantity);

    }

    @Test
    public void testGetAll() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);

        int quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barCode, quantity);

        dto.add(f);

        String barCode1 = "1a3t5tq5";
        String name1 = "name1";
        double mrp1 = 18.88;

        TestUtil.addProduct(barCode1, brand, category, name1, mrp1);

        InventoryForm f1 = TestUtil.getInventoryForm(barCode1, quantity);

        dto.add(f1);

        List<InventoryData> d = dto.getAll();

        assertEquals(d.size(), 2);

    }

    @Test
    public void testUpdate() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);

        int quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barCode, quantity);

        dto.add(f);

        int updatedQuantity = 10;
        f.setQuantity(updatedQuantity);

        dto.update(f);

        List<InventoryData> d = dto.getAll();
        assertEquals(d.get(0).getQuantity(), updatedQuantity);
    }

    @Test
    public void testAddProductNotExist() throws ApiException{
        boolean thrown = false;
        String barCode = "1a3t5tq8";

        int quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barCode, quantity);

        try{
            dto.add(f);
        }
        catch(ApiException e){
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testAddInventoryExists() throws ApiException{
        boolean thrown = false;
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);

        int quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barCode, quantity);

        dto.add(f);

        try{
            dto.add(f);
        }
        catch(ApiException e){
            thrown = true;
        }
        assertTrue(thrown);

    }

    @Test
    public void testUpdateInventoryNotExist() throws ApiException{
        boolean thrown = false;
        String barCode = "1a3t5tq8";

        int quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barCode, quantity);

        try{
            dto.update(f);
        }
        catch(ApiException e){
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testAddNegativeQuantity() throws ApiException{
        boolean thrown = false;
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);

        int quantity = -5;

        InventoryForm f = TestUtil.getInventoryForm(barCode, quantity);

        try{
            dto.add(f);
        }
        catch(ApiException e){
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testUpdateNegativeQuantity() throws ApiException{
        boolean thrown = false;
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barCode = "1a3t5tq8";
        String name = "name1";
        double mrp = 18.88;

        TestUtil.addProduct(barCode, brand, category, name, mrp);

        int quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barCode, quantity);

        dto.add(f);

        int updatedQuantity = -5;
        f.setQuantity(updatedQuantity);

        try{
            dto.update(f);
        }
        catch(ApiException e){
            thrown = true;
        }

        assertTrue(thrown);
    }
}
