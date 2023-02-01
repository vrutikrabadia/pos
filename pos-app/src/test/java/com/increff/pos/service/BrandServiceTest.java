package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.pojo.BrandPojo;

public class BrandServiceTest extends AbstractUnitTest {

    @Autowired
    private BrandService service;
    
    @Autowired
    private TestUtil testUtil;

    
    @Test
    public void testAddAndGet() throws ApiException {
        Integer id = testUtil.addBrand("brand", "category");
        BrandPojo p1 = service.get(id);
        assertEquals("brand", p1.getBrand());
        assertEquals("category", p1.getCategory());
    }

    @Test
    public void testGetAllPaginated() throws ApiException {
        testUtil.addBrand("brand1", "category1");
        testUtil.addBrand("brand2", "category2");
        assertEquals(1, service.getAllPaginated(0, 1).size());
        assertEquals(1, service.getAllPaginated(1, 1).size());
    }

    @Test
    public void testUpdate() throws ApiException {
        Integer id = testUtil.addBrand("brand", "category");
        BrandPojo p1 = service.get(id);
        p1.setBrand("brand1");
        p1.setCategory("category1");
        service.update(id, p1);
        BrandPojo p2 = service.get(id);
        assertEquals("brand1", p2.getBrand());
        assertEquals("category1", p2.getCategory());
    }

    @Test
    public void testGetByBrandCategory() throws ApiException {
        testUtil.addBrand("brand", "category");
        BrandPojo p1 = service.get("brand", "category");
        assertEquals("brand", p1.getBrand());
        assertEquals("category", p1.getCategory());
    }

    @Test
    public void testBulkAdd() throws ApiException {
        BrandPojo p1 = new BrandPojo();
        p1.setBrand("brand1");
        p1.setCategory("category1");
        BrandPojo p2 = new BrandPojo();
        p2.setBrand("brand2");
        p2.setCategory("category2");
        service.bulkAdd(Arrays.asList(p1, p2));
        assertEquals(2, service.getAll().size());
    }

    @Test
    public void testGetCheck() throws ApiException {
        Integer id = testUtil.addBrand("brand", "category");
        BrandPojo p1 = service.getCheck(id);
        assertEquals("brand", p1.getBrand());
        assertEquals("category", p1.getCategory());

        try {
            service.getCheck(100);
        } catch (ApiException e) {
            return;
        }

        fail();

    }

    @Test
    public void testGetByBrandCategoryCheck() throws ApiException {
        testUtil.addBrand("brand", "category");
        BrandPojo p1 = service.getcheck("brand", "category");
        assertEquals("brand", p1.getBrand());
        assertEquals("category", p1.getCategory());

        try {
            service.getcheck("brand1", "category1");
        } catch (ApiException e) {
            return;
        }

        fail();
    }

    @Test
    public void testSearchQueryString() {
        testUtil.addBrand("brand", "category");
        assertEquals(1, service.searchQueryString(0, 5, "brand").size());
        assertEquals(1, service.searchQueryString(0, 5, "category").size());
        assertEquals(0, service.searchQueryString(0, 5, "brand1").size());
    }

    @Test
    public void testGetTotalEntries() {
        testUtil.addBrand("brand", "category");
        testUtil.addBrand("brand1", "category1");

        Integer expectedEntries = 2;
        assertEquals(expectedEntries, service.getTotalEntries());
    }

    @Test
    public void testGetInColumn() {
        testUtil.addBrand("brand", "category");
        testUtil.addBrand("brand", "category1");

        assertEquals(2, service.getInColumn(Arrays.asList("brand"), Arrays.asList(Arrays.asList("brand"))).size());
        assertEquals(1,
                service.getInColumn(Arrays.asList("category"), Arrays.asList(Arrays.asList("category"))).size());
        assertEquals(1, service.getInColumn(Arrays.asList("brand", "category"),
                Arrays.asList(Arrays.asList("brand"), Arrays.asList("category"))).size());

    }

}
