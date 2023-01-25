package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.DateTimeProvider;

public class ReportDtoTest extends AbstractUnitTest{
    
    @Autowired
    private ReportDto dto;

    //brand report tests

    @Test
    public void testGetBrandReport(){
        
        //add brand
        TestUtil.addBrand("brand1", "category1");
        TestUtil.addBrand("brand2", "category2");
        
        String brandReport = dto.getBrandReport();

        assertNotNull(brandReport);

    }

    //inventory report tests

    @Test
    public void testgetProductIdToBrandCatMap(){
        //add brand

        Integer brandId1 =TestUtil.addBrand("brand1", "category1");
        Integer brandId2 =TestUtil.addBrand("brand2", "category2");

        Integer productId1 = TestUtil.addProduct("abcdefgh", brandId2, "p1", 10.00);
        Integer productId2 = TestUtil.addProduct("abcdefg1", brandId1, "p2", 10.00);

        List<InventoryPojo> iList = new ArrayList<InventoryPojo>();

        iList.add(TestUtil.getInventoryPojo(productId1, 5));
        iList.add(TestUtil.getInventoryPojo(productId2, 10));

        HashMap<Integer,Integer> map = dto.getProductIdToBrandCatMap(iList);


        assertEquals(brandId2, map.get(productId1));
        assertEquals(brandId1, map.get(productId2));
        
    }

    @Test
    public void testgetbrandCatIdToQuantityMap(){
        Integer brandId1 =TestUtil.addBrand("brand1", "category1");
        Integer brandId2 =TestUtil.addBrand("brand2", "category2");

        Integer productId1 = TestUtil.addProduct("abcdefgh", brandId2, "p1", 10.00);
        Integer productId2 = TestUtil.addProduct("abcdefg1", brandId1, "p2", 10.00);

        List<InventoryPojo> iList = new ArrayList<InventoryPojo>();

        iList.add(TestUtil.getInventoryPojo(productId1, 5));
        iList.add(TestUtil.getInventoryPojo(productId2, 10));

        HashMap<Integer,Integer> map = dto.getProductIdToBrandCatMap(iList);

        List<Integer> brandCatIdList = new ArrayList<Integer>(map.values());

        HashMap<Integer,Integer> brandCatIdToQuantityMap = dto.getbrandCatIdToQuantityMap(map, iList, brandCatIdList);


        assertEquals(10, brandCatIdToQuantityMap.get(brandId1).intValue());
        assertEquals(5, brandCatIdToQuantityMap.get(brandId2).intValue());
    }

    @Test
    public void testGetInventoryReport() throws ApiException{
        Integer brandId1 =TestUtil.addBrand("brand1", "category1");
        Integer brandId2 =TestUtil.addBrand("brand2", "category2");

        Integer productId1 = TestUtil.addProduct("abcdefgh", brandId2, "p1", 10.00);
        Integer productId2 = TestUtil.addProduct("abcdefg1", brandId1, "p2", 10.00);

        TestUtil.addInventory(productId1, 5);
        TestUtil.addInventory(productId2, 10);

        String inventoryReport = dto.getInventoryReport();

        assertNotNull(inventoryReport);
    }

    //Sales report tests
    @Test
    public void testGetOrderItems(){
        Integer brandId1 =TestUtil.addBrand("brand1", "category1");
        Integer brandId2 =TestUtil.addBrand("brand2", "category2");

        Integer productId1 = TestUtil.addProduct("abcdefgh", brandId2, "p1", 10.00);
        Integer productId2 = TestUtil.addProduct("abcdefg1", brandId1, "p2", 10.00);

        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0);
        DateTimeProvider.getInstance().setTime(customAppTime);
        Integer orderId = TestUtil.addOrder();

        DateTimeProvider.getInstance().setTime(customAppTime.minusDays(1));
        Integer orderId1 = TestUtil.addOrder();

        TestUtil.addOrderItem(orderId, productId1, 5, 10.00);
        TestUtil.addOrderItem(orderId, productId2, 5, 10.00);
        TestUtil.addOrderItem(orderId1, productId1, 5, 10.00);
        TestUtil.addOrderItem(orderId1, productId2, 5, 10.00);
        
        DateTimeProvider.getInstance().setTime(customAppTime.plusDays(2));

        List<OrderItemsPojo> itemsList = dto.getOrderItems(DateTimeProvider.getInstance().timeNow().minusDays(2), DateTimeProvider.getInstance().timeNow().plusDays(1), null);
        assertEquals(2, itemsList.size());

        itemsList = dto.getOrderItems(DateTimeProvider.getInstance().timeNow().minusDays(3), DateTimeProvider.getInstance().timeNow().plusDays(1), null);
        assertEquals(4, itemsList.size());
        
        itemsList = dto.getOrderItems(DateTimeProvider.getInstance().timeNow().minusDays(3), DateTimeProvider.getInstance().timeNow().plusDays(1), Arrays.asList(productId1));
        assertEquals(2, itemsList.size());
    
    }

    @Test
    public void testSalesReportWithoutBrandAndCategory() throws ApiException{
        Integer brandId1 =TestUtil.addBrand("brand1", "category1");
        Integer brandId2 =TestUtil.addBrand("brand2", "category2");

        Integer productId1 = TestUtil.addProduct("abcdefgh", brandId2, "p1", 10.00);
        Integer productId2 = TestUtil.addProduct("abcdefg1", brandId1, "p2", 10.00);

        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0);
        DateTimeProvider.getInstance().setTime(customAppTime);
        Integer orderId = TestUtil.addOrder();

        DateTimeProvider.getInstance().setTime(customAppTime.minusDays(1));
        Integer orderId1 = TestUtil.addOrder();

        TestUtil.addOrderItem(orderId, productId1, 5, 10.00);
        TestUtil.addOrderItem(orderId, productId2, 5, 10.00);
        TestUtil.addOrderItem(orderId1, productId1, 5, 10.00);
        TestUtil.addOrderItem(orderId1, productId2, 5, 10.00);
        
        SalesReportForm form = TestUtil.getSalesReportForm(customAppTime.minusDays(3), customAppTime.plusDays(1), null, null);

        String salesReport = dto.getSalesReport(form);

        assertNotNull(salesReport);

    }
    
    @Test
    public void testSalesReportWithBrandAndCategory() throws ApiException{
        Integer brandId1 =TestUtil.addBrand("brand1", "category1");
        Integer brandId2 =TestUtil.addBrand("brand2", "category2");

        Integer productId1 = TestUtil.addProduct("abcdefgh", brandId2, "p1", 10.00);
        Integer productId2 = TestUtil.addProduct("abcdefg1", brandId1, "p2", 10.00);

        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0);
        DateTimeProvider.getInstance().setTime(customAppTime);
        Integer orderId = TestUtil.addOrder();

        DateTimeProvider.getInstance().setTime(customAppTime.minusDays(1));
        Integer orderId1 = TestUtil.addOrder();

        TestUtil.addOrderItem(orderId, productId1, 5, 10.00);
        TestUtil.addOrderItem(orderId, productId2, 5, 10.00);
        TestUtil.addOrderItem(orderId1, productId1, 5, 10.00);
        TestUtil.addOrderItem(orderId1, productId2, 5, 10.00);
        
        SalesReportForm form = TestUtil.getSalesReportForm(customAppTime.minusDays(3), customAppTime.plusDays(1), "brand1", null);
        String salesReport = dto.getSalesReport(form);
        assertNotNull(salesReport);

        form = TestUtil.getSalesReportForm(customAppTime.minusDays(3), customAppTime.plusDays(1), null, "category1");
        salesReport = dto.getSalesReport(form);
        assertNotNull(salesReport);

        form = TestUtil.getSalesReportForm(customAppTime.minusDays(3), customAppTime.plusDays(1), "brand1", "category1");
        salesReport = dto.getSalesReport(form);
        assertNotNull(salesReport);
    }

}
