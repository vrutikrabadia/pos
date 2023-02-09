package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.config.TestUtil;
import com.increff.pos.pojo.ProductPojo;

public class ProductServiceTest extends AbstractUnitTest {

    @Autowired
    private ProductService service;
    
    @Autowired
    private TestUtil testUtil;

    @Test
    public void testAdd() throws ApiException {
        Integer id = testUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        ProductPojo p1 = service.getCheckById(id);
        assertEquals("n1", p1.getName());
        assertEquals("abcdefgh", p1.getBarcode());
        assertEquals(10.00, p1.getMrp(), 0.001);
    }

    @Test
    public void testGetAll() throws ApiException {
        testUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        testUtil.addBrandAndProduct("b2", "c2", "abcdefg1", "n2", 10.00);
        assertEquals(2, service.getAll().size());
    }

    @Test
    public void testGetAllPaginated() throws ApiException {
        testUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        testUtil.addBrandAndProduct("b2", "c2", "abcdefg1", "n2", 10.00);
        assertEquals(1, service.getAllPaginated(0, 1).size());
        assertEquals(1, service.getAllPaginated(1, 1).size());
    }

    @Test
    public void testUpdate() throws ApiException {
        Integer id = testUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        ProductPojo p1 = service.getCheckById(id);
        p1.setName("name1");
        p1.setBarcode("barcode1");
        p1.setMrp(100.00);
        service.update(id, p1);
        ProductPojo p2 = service.getCheckById(id);
        assertEquals("name1", p2.getName());
        assertEquals("barcode1", p2.getBarcode());
        assertEquals(100.00, p2.getMrp(), 0.001);
    }

    @Test
    public void testGetByBarcode() throws ApiException {
        testUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        ProductPojo p1 = service.getCheckByBarCode("abcdefgh");
        assertEquals("n1", p1.getName());
        assertEquals("abcdefgh", p1.getBarcode());
        assertEquals(10.00, p1.getMrp(), 0.001);
    }

    @Test
    public void testGetById() throws ApiException {
        Integer id = testUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        ProductPojo p1 = service.getCheckById(id);
        assertEquals("n1", p1.getName());
        assertEquals("abcdefgh", p1.getBarcode());
        assertEquals(10.00, p1.getMrp(), 0.001);
    }

    @Test
    public void testGetByBrandCategory() {
        Integer brandId = testUtil.addBrand("b1", "c1");
        testUtil.addProduct("abcdefgh", brandId, "n1", 10.00);
        testUtil.addProduct("abcdefg1", brandId, "n2", 10.00);
        List<ProductPojo> p1 = service.getByBrandCatId(brandId);

        assertEquals(2, p1.size());
    }

    @Test
    public void testGetByQueryString() {
        Integer brandId = testUtil.addBrand("b1", "c1");
        testUtil.addProduct("abcdefgh", brandId, "n1", 10.00);
        testUtil.addProduct("a1cdefgh", brandId, "n2", 10.00);
        List<ProductPojo> p1 = service.getByQueryString(0, 5, "n1");

        assertEquals(1, p1.size());

        p1 = service.getByQueryString(0, 5, "abc");
        assertEquals(1, p1.size());
    }

    @Test
    public void testGetTotalEntries() {
        Integer brandId = testUtil.addBrand("b1", "c1");
        testUtil.addProduct("abcdefgh", brandId, "n1", 10.00);
        testUtil.addProduct("a1cdefgh", brandId, "n2", 10.00);

        Integer expectedCount = 2;
        assertEquals(expectedCount, service.getTotalEntries());
    }

    @Test
    public void testGetInColumn() throws ApiException {
        Integer brandId = testUtil.addBrand("b1", "c1");
        testUtil.addProduct("abcdefgh", brandId, "n1", 10.00);
        testUtil.addProduct("a1cdefgh", brandId, "n2", 10.00);

        List<ProductPojo> p1 = service.getInColumns(Arrays.asList("name"), Arrays.asList(Arrays.asList("n1", "n2")));
        assertEquals(2, p1.size());
    }

    @Test
    public void testGetBarcodeNotExists() {
        testUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);

        try {
            service.getCheckByBarCode("abcdefg1");
        } catch (ApiException e) {
            return;
        }

        fail();
    }

    @Test 
    public void testGeProductNotExists(){
        testUtil.addBrandAndProduct("b1", "c1", "abcdefgh", "n1", 10.00);
        try{
            service.getCheckById(99999999);
        }
        catch(ApiException e){
            return;
        }

        fail();

    }


}
