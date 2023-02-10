package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.config.TestUtil;
import com.increff.pos.pojo.InventoryPojo;

public class InventoryServiceTest extends AbstractUnitTest {

    @Autowired
    private InventoryService service;
    
    @Autowired
    private TestUtil testUtil;

    @Test
    public void testAddAndGet() {
        Integer prodId = testUtil.addBrandAndProduct("b1", "b2", "abcdefgh", "p1", 10.00);
        testUtil.addInventory(prodId, 10);
        InventoryPojo p = service.get(prodId);
        assertEquals(prodId, p.getProductId());
        assertEquals(Integer.valueOf(10), p.getQuantity());

    }

    @Test
    public void testAddAndGetAll() {
        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n2", 10.00, 10);
        testUtil.addBrandProductAndInventory("b3", "c3", "abcdefg2", "n3", 10.00, 10);
        List<InventoryPojo> list = service.getAll();
        assertEquals(3, list.size());
    }

    @Test
    public void testAddAndGetAllPaginated() {
        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n2", 10.00, 10);
        testUtil.addBrandProductAndInventory("b3", "c3", "abcdefg2", "n3", 10.00, 10);
        assertEquals(1, service.getAllPaginated(0, 1).size());
    }

    @Test
    public void testAddAndUpdate() throws ApiException {
        Integer prodId = testUtil.addBrandAndProduct("b1", "b2", "abcdefgh", "p1", 10.00);
        testUtil.addInventory(prodId, 10);
        InventoryPojo p = service.get(prodId);
        p.setQuantity(20);
        service.update(prodId, p);
        assertEquals(Integer.valueOf(20), service.get(prodId).getQuantity());
    }

    @Test
    public void testReduceQuantity() throws ApiException {
        Integer prodId = testUtil.addBrandAndProduct("b1", "b2", "abcdefgh", "p1", 10.00);
        testUtil.addInventory(prodId, 10);
        service.reduceQuantity(prodId, 5);
        assertEquals(Integer.valueOf(5), service.get(prodId).getQuantity());
    }

    @Test
    public void testIncreaseQuantity() throws ApiException {
        Integer prodId = testUtil.addBrandAndProduct("b1", "b2", "abcdefgh", "p1", 10.00);
        testUtil.addInventory(prodId, 10);
        service.increaseQuantity(prodId, 5);
        assertEquals(Integer.valueOf(15), service.get(prodId).getQuantity());
    }

    @Test
    public void testGetTotalEntries(){
        testUtil.addBrandProductAndInventory("b1", "c1", "abcdefgh", "n1", 10.00, 10);
        testUtil.addBrandProductAndInventory("b2", "c2", "abcdefg1", "n2", 10.00, 10);
        testUtil.addBrandProductAndInventory("b3", "c3", "abcdefg2", "n3", 10.00, 10);
        assertEquals(Integer.valueOf(3), service.getTotalEntries());
    }

}
