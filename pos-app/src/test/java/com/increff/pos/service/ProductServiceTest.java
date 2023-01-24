package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.pojo.ProductPojo;

public class ProductServiceTest extends AbstractUnitTest{
    
    @Autowired
    private ProductService service;


    @Test
    public void testAdd() throws ApiException {
        Integer id = TestUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        ProductPojo p1 = service.get(id);
        assertEquals("n1", p1.getName());
        assertEquals("abcdefgh", p1.getBarcode());
        assertEquals(10.00, p1.getMrp(), 0.001);
    }


    @Test
    public void testGetAll() throws ApiException {
        TestUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        TestUtil.addBrandAndProduct("b2", "c2", "abcdefg1", "n2", 10.00);
        assertEquals(2, service.getAll().size());
    }

    @Test
    public void testGetAllPaginated() throws ApiException {
        TestUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        TestUtil.addBrandAndProduct("b2", "c2", "abcdefg1", "n2", 10.00);
        assertEquals(1, service.getAllPaginated(0, 1).size());
        assertEquals(1, service.getAllPaginated(1, 1).size());
    }

    @Test
    public void testUpdate() throws ApiException {
        Integer id = TestUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        ProductPojo p1 = service.get(id);
        p1.setName("name1");
        p1.setBarcode("barcode1");
        p1.setMrp(100.00);
        service.update(id, p1);
        ProductPojo p2 = service.get(id);
        assertEquals("name1", p2.getName());
        assertEquals("barcode1", p2.getBarcode());
        assertEquals(100.00, p2.getMrp(), 0.001);
    }

    @Test
    public void testGetByBarcode() throws ApiException {
        TestUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        ProductPojo p1 = service.get("abcdefgh");
        assertEquals("n1", p1.getName());
        assertEquals("abcdefgh", p1.getBarcode());
        assertEquals(10.00, p1.getMrp(), 0.001);
    }

    @Test
    public void testGetById() throws ApiException {
        Integer id = TestUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        ProductPojo p1 = service.get(id);
        assertEquals("n1", p1.getName());
        assertEquals("abcdefgh", p1.getBarcode());
        assertEquals(10.00, p1.getMrp(), 0.001);
    }

    @Test
    public void testGetByBrandCategory(){
        Integer brandId = TestUtil.addBrand("b1", "c1");
        TestUtil.addProduct("abcdefgh",brandId, "n1", 10.00);
        TestUtil.addProduct("abcdefg1",brandId, "n2", 10.00);
        List<ProductPojo> p1 = service.getByBrandCat(brandId);

        assertEquals(2, p1.size());
    }

    @Test
    public void testGetByQueryString(){
        Integer brandId = TestUtil.addBrand("b1", "c1");
        TestUtil.addProduct("abcdefgh",brandId, "n1", 10.00);
        TestUtil.addProduct("a1cdefgh",brandId, "n2", 10.00);
        List<ProductPojo> p1 = service.getByQueryString(0,5,"n1");

        assertEquals(1, p1.size());
        
        p1 = service.getByQueryString(0,5,"abc");
        assertEquals(1, p1.size());
    }

    @Test
    public void testGetTotalEntries(){
        Integer brandId = TestUtil.addBrand("b1", "c1");
        TestUtil.addProduct("abcdefgh",brandId, "n1", 10.00);
        TestUtil.addProduct("a1cdefgh",brandId, "n2", 10.00);
        
        Integer expectedCount = 2;
        assertEquals(expectedCount, service.getTotalEntries());
    }

    @Test
    public void testGetInColumn(){
        Integer brandId = TestUtil.addBrand("b1", "c1");
        TestUtil.addProduct("abcdefgh",brandId, "n1", 10.00);
        TestUtil.addProduct("a1cdefgh",brandId, "n2", 10.00);
        
        List<ProductPojo> p1 = service.getInColumn(Arrays.asList("name"), Arrays.asList(Arrays.asList("n1", "n2")));
        assertEquals(2, p1.size());
    }
}
