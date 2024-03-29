package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.config.TestUtil;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.util.ApiException;

public class InventoryDtoTest extends AbstractUnitTest{
    
    @Autowired
    private InventoryDto dto;
    
    @Autowired
    private TestUtil testUtil;


    @Test
    public void testAddAndGet() throws ApiException{
        String brand = "brand1";
        String category = "category1";
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        testUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);
        
        dto.add(f);

        List<InventoryData> d = dto.getAll(0,1,1,null).getData();
        
        assertEquals(d.get(0).getBarcode(), barcode);
        assertEquals(d.get(0).getQuantity(), quantity);

    }

    @Test
    public void testGetAll() throws ApiException{
        String brand = "brand1";
        String category = "category1";
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        Integer brandId = testUtil.addBrand(brand, category);
        testUtil.addProduct(barcode, brandId, name, mrp);

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        String barcode1 = "1a3t5tq5";
        String name1 = "name1";
        Double mrp1 = 18.88;

        testUtil.addProduct(barcode1, brandId, name1, mrp1);

        InventoryForm f1 = testUtil.getInventoryForm(barcode1, quantity);

        dto.add(f1);

        List<InventoryData> d = dto.getAll(0,10,1,null).getData();

        assertEquals(d.size(), 2);

    }

    @Test
    public void testUpdate() throws ApiException{
        String brand = "brand1";
        String category = "category1";
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        testUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        Integer updatedQuantity = 10;
        f.setQuantity(updatedQuantity);

        dto.update(f);

        List<InventoryData> d = dto.getAll(0,1,1,null).getData();
        assertEquals(d.get(0).getQuantity(), updatedQuantity);
    }

    @Test
    public void testAddProductNotExist() throws ApiException{
        String barcode = "1a3t5tq8";

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

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
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        testUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

        dto.add(f);
        dto.add(f);
        List<InventoryData> d = dto.getAll(0,1, 1, null).getData();
        
        Integer finalQuantity = 10;

        assertEquals(d.get(0).getBarcode(), barcode);
        assertEquals(d.get(0).getQuantity(), finalQuantity);


    }

    @Test
    public void testUpdateInventoryNotExist() throws ApiException{
        String barcode = "1a3t5tq8";

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

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
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        testUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        Integer quantity = -5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

        try{
            dto.add(f);
        }
        catch(ConstraintViolationException e){
            return;
        }
        fail();
    }

    @Test
    public void testUpdateNegativeQuantity() throws ApiException{
        String brand = "brand1";
        String category = "category1";
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        testUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        Integer updatedQuantity = -5;
        f.setQuantity(updatedQuantity);

        try{
            dto.update(f);
        }
        catch(ConstraintViolationException e){
            return;
        }
        fail();

    }

    @Test
    public void testGetByBarCode() throws ApiException{
        String brand = "brand1";
        String category = "category1";
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        testUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        InventoryData data = dto.get(barcode);

        assertEquals(quantity, data.getQuantity());

    }

    @Test
    public void testGetBySearchValue() throws ApiException{
        String brand = "brand1";
        String category = "category1";
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        testUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);

        dto.add(f);

        SelectData<InventoryData> data = dto.getAll(0,5,0,"1a");

        assertEquals(1, data.getData().size());
    }


    @Test
    public void testAddInventoryBulk() throws ApiException{
        String brand = "brand1";
        String category = "category1";
        String barcode = "1a3t5tq8";
        String name = "name1";
        Double mrp = 18.88;

        Integer brandId = testUtil.addBrandAndProduct(brand, category, barcode, name, mrp);

        Integer quantity = 5;

        InventoryForm f = testUtil.getInventoryForm(barcode, quantity);


        String barcode1 = "1a3t5tq5";
        String name1 = "name1";
        Double mrp1 = 18.88;

        testUtil.addProduct(barcode1, brandId, name1, mrp1);

        InventoryForm f1 = testUtil.getInventoryForm(barcode1, quantity);


        List<InventoryForm> forms = new ArrayList<InventoryForm>();
        forms.add(f);
        forms.add(f1);

        dto.bulkAdd(forms);

        List<InventoryData> d = dto.getAll(0,2,1,null).getData();
        assertEquals(d.get(0).getQuantity(), quantity);
        assertEquals(d.get(1).getQuantity(), quantity);

    }
}
