package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);

        Integer quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        List<InventoryData> d = dto.getAll(0,1,1).getData();
        
        assertEquals(d.get(0).getBarcode(), barcode);
        assertEquals(d.get(0).getQuantity(), quantity);

    }

    @Test
    public void testGetAll() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);

        Integer quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        String barcode1 = "1a3t5tq5";
        String name1 = "name1";
        Double mrp1 = 18.88;

        TestUtil.addProduct(barcode1, brand, category, name1, mrp1);

        InventoryForm f1 = TestUtil.getInventoryForm(barcode1, quantity);

        dto.add(f1);

        List<InventoryData> d = dto.getAll(0,10,1).getData();

        assertEquals(d.size(), 2);

    }

    @Test
    public void testUpdate() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);

        Integer quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        Integer updatedQuantity = 10;
        f.setQuantity(updatedQuantity);

        dto.update(f);

        List<InventoryData> d = dto.getAll(0,1,1).getData();
        assertEquals(d.get(0).getQuantity(), updatedQuantity);
    }

    @Test
    public void testAddProductNotExist() throws ApiException{
        String barcode = "1a3t5tq8";

        Integer quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barcode, quantity);

        try{
            dto.add(f);
        }
        catch(ApiException e){
            return;
        }
        fail();
    }

    @Test
    public void testAddInventoryExists() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);

        Integer quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barcode, quantity);

        dto.add(f);
        dto.add(f);
        List<InventoryData> d = dto.getAll(0,1, 1).getData();
        
        Integer finalQuantity = 10;

        assertEquals(d.get(0).getBarcode(), barcode);
        assertEquals(d.get(0).getQuantity(), finalQuantity);


    }

    @Test
    public void testUpdateInventoryNotExist() throws ApiException{
        String barcode = "1a3t5tq8";

        Integer quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barcode, quantity);

        try{
            dto.update(f);
        }
        catch(ApiException e){
            return;
        }
        fail();
    }

    @Test
    public void testAddNegativeQuantity() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);

        Integer quantity = -5;

        InventoryForm f = TestUtil.getInventoryForm(barcode, quantity);

        try{
            dto.add(f);
        }
        catch(ApiException e){
            return;
        }
        fail();
    }

    @Test
    public void testUpdateNegativeQuantity() throws ApiException{
        String brand = "brand1";
        String category = "category1";

        TestUtil.addBrand(brand, category);

        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        TestUtil.addProduct(barcode, brand, category, name, mrp);

        Integer quantity = 5;

        InventoryForm f = TestUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        Integer updatedQuantity = -5;
        f.setQuantity(updatedQuantity);

        try{
            dto.update(f);
        }
        catch(ApiException e){
            return;
        }
        fail();

    }
}
