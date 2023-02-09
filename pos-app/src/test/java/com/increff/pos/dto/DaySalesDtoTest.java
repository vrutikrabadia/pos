package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.config.TestUtil;
import com.increff.pos.model.data.DaySalesData;
import com.increff.pos.model.data.SelectData;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.DateTimeProvider;


public class DaySalesDtoTest extends AbstractUnitTest{


    @Autowired
    private DaySalesDto dto;

    
    @Autowired
    private TestUtil testUtil;


    @Test
    public void testGetAll() throws ApiException{
        ZonedDateTime start = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        testUtil.addDaySales(start, 5, 5, 2500.0);
        testUtil.addDaySales(start.minusDays(1), 5, 5, 2500.0);

        SelectData<DaySalesData> daySalesList = dto.getAll(0, 5, 1);

        assertEquals(2, daySalesList.getData().size());
    }

    @Test
    public void testGetInDateRange() throws ApiException{
        ZonedDateTime start = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        testUtil.addDaySales(start, 5, 5, 2500.0);
        testUtil.addDaySales(start.minusDays(1), 5, 5, 2500.0);
        testUtil.addDaySales(start.minusDays(2), 5, 5, 2500.0);
        testUtil.addDaySales(start.minusDays(3), 5, 5, 2500.0);

        SelectData<DaySalesData> daySalesList = dto.getInDateRange(0, 5, 1,start.minusDays(2), start);

        assertEquals(3, daySalesList.getData().size());
    }

    @Test
    public void testScheduler() throws ApiException{
        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().minusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0);
        DateTimeProvider.getInstance().setTime(customAppTime);

        testUtil.createOrder("b1", "c1", "abcdefgh", "n1", 10.00, 10, 5, 15.00);
        testUtil.createOrder("b2", "c2", "abcdefg1", "n1", 10.00, 10, 5, 15.00);

        DateTimeProvider.getInstance().setTime(customAppTime.plusDays(1));

        dto.daySalesScheduler();

        SelectData<DaySalesData> daySalesList = dto.getAll(0, 5, 1);

        assertEquals(1, daySalesList.getData().size());
        
    }





}
