package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.DateTimeProvider;

public class OrderServiceTest extends AbstractUnitTest{

    
    @Autowired
    private OrderService service;
    
    @Autowired
    private TestUtil testUtil;

    @Test
    public void testAddAndGet() throws ApiException{
        Integer orderId = testUtil.addOrder();

        OrderPojo p = service.get(orderId);

        assertEquals(orderId, p.getId());
    }

    @Test
    public void testGetAll(){
        Integer orderId = testUtil.addOrder();
        Integer orderId2 = testUtil.addOrder();

        List<OrderPojo> list = service.getAll();

        assertEquals(2, list.size());
    }

    @Test
    public void testGetAllPaginated(){
        Integer orderId = testUtil.addOrder();
        Integer orderId2 = testUtil.addOrder();

        List<OrderPojo> list = service.getAllPaginated(0, 1);

        assertEquals(1, list.size());
    }

    @Test
    public void testGetCheck() throws ApiException{
        Integer orderId = testUtil.addOrder();

        OrderPojo p = service.getCheck(orderId);

        assertEquals(orderId, p.getId());
    }

    @Test(expected = ApiException.class)
    public void testGetCheckException() throws ApiException{
        service.getCheck(1);
    }

    @Test
    public void testGetInDateRange(){
        Integer orderId = testUtil.addOrder();
        Integer orderId2 = testUtil.addOrder();

        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().plusDays(2).withHour(12).withMinute(0).withSecond(0).withNano(0);
        DateTimeProvider.getInstance().setTime(customAppTime);

        Integer orderId3 = testUtil.addOrder();

        customAppTime = DateTimeProvider.getInstance().timeNow().minusDays(2).withHour(12).withMinute(0).withSecond(0).withNano(0);
        DateTimeProvider.getInstance().setTime(customAppTime);

        List<OrderPojo> list = service.getInDateRange(DateTimeProvider.getInstance().timeNow().minusDays(1), DateTimeProvider.getInstance().timeNow().plusDays(1));

        assertEquals(2, list.size());
    }
}

